package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class PriorityScheduler implements Scheduler {
    // Sort by Priority (lower is higher priority). Then Arrival.
    private final PriorityQueue<Process> readyQueue = new PriorityQueue<>(
        Comparator.comparingInt(Process::getPriority)
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
        if (readyQueue.isEmpty()) return false;
        
        Process bestCandidate = readyQueue.peek();
        // Preempt if candidate has strictly higher priority (lower value)
        return bestCandidate.getPriority() < currentProcess.getPriority();
    }

    @Override
    public String getName() {
        return "Priority (Preemptive)";
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
