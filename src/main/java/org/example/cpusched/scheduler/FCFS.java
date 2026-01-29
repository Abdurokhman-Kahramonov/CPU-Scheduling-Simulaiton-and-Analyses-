package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;

public class FCFS implements Scheduler {
    private final Queue<Process> readyQueue = new LinkedList<>();

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
        // FCFS is non-preemptive
        return false;
    }

    @Override
    public String getName() {
        return "FCFS";
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
