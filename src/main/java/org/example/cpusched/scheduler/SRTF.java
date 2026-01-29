package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class SRTF implements Scheduler {
    // Sort by Remaining Time ascending.
    private final PriorityQueue<Process> readyQueue = new PriorityQueue<>(
        Comparator.comparingInt(Process::getRemainingTime)
                  .thenComparingInt(Process::getArrivalTime)
    );

    @Override
    public void onProcessArrival(Process process) {
        readyQueue.add(process);
    }

    @Override
    public Process pickNextProcess(int currentTime) {
        return readyQueue.poll();
    }

    @Override
    public boolean shouldPreempt(Process currentProcess, int currentTime, int quantumElapsed) {
        // Preemptive: If a new process arrives with shorter remaining time than current,
        // schedule() will return the new head. 
        // The engine logic will call schedule(), and if it returns a different process, it preempts.
        // However, we need to handle the case where 'currentProcess' is NOT in the queue because it's running.
        // The Engine typically puts the running process back into consideration or asks "should I switch?".
        
        // Strategy: if the ready queue head is strictly better than current, preempt.
        if (readyQueue.isEmpty()) return false;
        
        Process bestCandidate = readyQueue.peek();
        // Strict inequality to avoid context switch on ties (reduces overhead)
        return bestCandidate.getRemainingTime() < currentProcess.getRemainingTime();
    }

    @Override
    public String getName() {
        return "SRTF";
    }

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty();
    }
    
    @Override
    public List<Process> getReadyQueueSnapshot() {
        return new ArrayList<>(readyQueue);
    }
}
