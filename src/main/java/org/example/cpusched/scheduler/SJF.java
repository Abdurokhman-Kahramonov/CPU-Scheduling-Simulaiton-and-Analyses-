package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class SJF implements Scheduler {
    // Sort by Burst Time ascending. If tie, use Arrival Time (PID implicitly via Process.compareTo if needed, or consistent order)
    private final PriorityQueue<Process> readyQueue = new PriorityQueue<>(
        Comparator.comparingInt(Process::getBurstTime)
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
        // SJF is non-preemptive
        return false;
    }

    @Override
    public String getName() {
        return "SJF";
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
