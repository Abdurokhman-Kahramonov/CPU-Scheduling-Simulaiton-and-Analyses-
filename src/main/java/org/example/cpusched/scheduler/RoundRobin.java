package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class RoundRobin implements Scheduler {
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final int timeQuantum;

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

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
        // Preempt if quantum expired AND there are other processes waiting
        return quantumElapsed >= timeQuantum && !readyQueue.isEmpty();
    }

    @Override
    public String getName() {
        return "Round Robin (Q=" + timeQuantum + ")";
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
