package org.example.cpusched.scheduler;

import org.example.cpusched.model.Process;
import java.util.List;

/**
 * Interface for CPU Scheduling Algorithms.
 * Strategies implement this to define how processes are selected for execution.
 */
public interface Scheduler {
    
    /**
     * Called when a new process arrives in the system.
     * @param process The newly arrived process.
     */
    void onProcessArrival(Process process);

    /**
     * Selects the next process to run on the CPU and removes it from the ready queue.
     * @param currentTime The current simulation time unit.
     * @return The process to run, or null if the ready queue is empty.
     */
    Process pickNextProcess(int currentTime);

    /**
     * Checks if the currently running process should be preempted.
     * This is critical for preemptive algorithms (SRTF, RR, Priority, MLFQ).
     * 
     * @param currentProcess The process currently on the CPU.
     * @param currentTime Current simulation time.
     * @param quantumElapsed Time spent by the current process in the current quantum (if applicable).
     * @return true if the process should be preempted.
     */
    boolean shouldPreempt(Process currentProcess, int currentTime, int quantumElapsed);

    /**
     * Returns the name of the algorithm.
     */
    String getName();

    /**
     * Optional: Returns true if the ready queue is empty.
     */
    boolean isEmpty();
    
    /**
     * Optional: Allows inspecting the internal queue for debugging/logging.
     */
    List<Process> getReadyQueueSnapshot();
}
