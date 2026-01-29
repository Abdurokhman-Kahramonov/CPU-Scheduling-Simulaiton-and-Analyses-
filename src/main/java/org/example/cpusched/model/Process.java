package org.example.cpusched.model;

/**
 * Represents a process in the CPU scheduling simulation.
 * This class acts as a data container for process attributes and runtime statistics.
 */
public class Process implements Comparable<Process> {
    private final int pid;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority; // Lower value = Higher priority

    // Runtime state
    private int remainingTime;
    private int startTime = -1;
    private int completionTime = -1;
    private int totalWaitingTime = 0;
    private int lastActiveTime = -1; // To calculate waiting time during simulation

    // For MLFQ or other complex schedulers
    private int currentQueueLevel = 0;
    private int timeInCurrentQueue = 0;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.lastActiveTime = arrivalTime; // Initially waits from arrival
    }

    // Copy constructor for simulation reproducibility if needed, 
    // but typically we just reset or generate new ones.
    
    public int getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public void decreaseRemainingTime(int amount) { this.remainingTime -= amount; }

    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getCompletionTime() { return completionTime; }
    public void setCompletionTime(int completionTime) { 
        this.completionTime = completionTime; 
        this.totalWaitingTime = (completionTime - arrivalTime) - burstTime;
    }

    public int getTotalWaitingTime() { return totalWaitingTime; }
    public void addWaitingTime(int time) { this.totalWaitingTime += time; }

    public int getCurrentQueueLevel() { return currentQueueLevel; }
    public void setCurrentQueueLevel(int level) { this.currentQueueLevel = level; }
    public void incrementQueueLevel() { this.currentQueueLevel++; }
    
    public int getTimeInCurrentQueue() { return timeInCurrentQueue; }
    public void setTimeInCurrentQueue(int time) { this.timeInCurrentQueue = time; }
    public void addTimeInCurrentQueue(int time) { this.timeInCurrentQueue += time; }

    public boolean isFinished() { return remainingTime <= 0; }

    public int getTurnaroundTime() {
        return completionTime - arrivalTime;
    }

    public int getResponseTime() {
        return startTime - arrivalTime;
    }

    @Override
    public int compareTo(Process o) {
        return Integer.compare(this.pid, o.pid);
    }

    @Override
    public String toString() {
        return "P" + pid + "[Arr=" + arrivalTime + ", Burst=" + burstTime + ", Prio=" + priority + "]";
    }
}
