package org.example.cpusched.engine;

import org.example.cpusched.metrics.MetricsCollector;
import org.example.cpusched.model.Process;
import org.example.cpusched.scheduler.Scheduler;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class SimulationEngine {
    private final Scheduler scheduler;
    private final List<Process> workload;
    private final MetricsCollector metrics;
    private final int contextSwitchCost;
    
    private int currentTime = 0;
    private Process currentProcess = null;
    private int quantumElapsed = 0;
    private int switchCountdown = 0;
    private boolean isSwitching = false;

    public SimulationEngine(Scheduler scheduler, List<Process> workload, MetricsCollector metrics, int contextSwitchCost) {
        this.scheduler = scheduler;
        this.workload = new ArrayList<>(workload); // Copy to avoid modifying original if needed
        this.workload.sort(Comparator.comparingInt(Process::getArrivalTime)); // Ensure sorted by arrival
        this.metrics = metrics;
        this.contextSwitchCost = contextSwitchCost;
    }

    public void run() {
        int completedCount = 0;
        int totalProcesses = workload.size();
        int workloadIndex = 0;

        while (completedCount < totalProcesses) {
            // 1. Check Arrivals
            while (workloadIndex < totalProcesses && workload.get(workloadIndex).getArrivalTime() <= currentTime) {
                Process p = workload.get(workloadIndex);
                scheduler.onProcessArrival(p);
                workloadIndex++;
            }

            // 2. Handle Context Switching
            if (isSwitching) {
                switchCountdown--;
                if (switchCountdown <= 0) {
                    isSwitching = false;
                    // Context switch done, start the pending process (stored in currentProcess)
                    // Wait, if I stored it in currentProcess, I just start running it.
                    // But if I was switching OUT, currentProcess might be null?
                    // Let's refine state: 
                    // State: IDLE, RUNNING, SWITCHING
                    // Logic:
                    // If switching, decrement. If 0, become RUNNING (if we have a process).
                } else {
                    currentTime++;
                    metrics.addIdleTime(1); // Arguably switching is overhead, not useful work. Count as idle/overhead? 
                    // Usually "CPU Utilization" excludes switch time. So yes.
                    continue; 
                }
            }

            // 3. Schedule if CPU is free
            if (currentProcess == null) {
                Process next = scheduler.pickNextProcess(currentTime);
                if (next != null) {
                    // Found a process. Is there a switch cost?
                    if (contextSwitchCost > 0) {
                        isSwitching = true;
                        switchCountdown = contextSwitchCost;
                        currentProcess = next; // We claim it, but don't run it yet.
                        metrics.incrementContextSwitches();
                        
                        // We need to consume this tick for switching? 
                        // If cost=1, we switch now, next tick we run?
                        // Let's say cost is applied NOW.
                        continue; // Go to next tick to process switch delay
                    } else {
                        // Instant switch
                        currentProcess = next;
                        metrics.incrementContextSwitches();
                    }
                } else {
                    // No process ready. IDLE.
                    currentTime++;
                    metrics.addIdleTime(1);
                    continue;
                }
            }

            // 4. Execute Process
            // If we are here, currentProcess is not null and we are not switching.
            
            // Mark start time if first run
            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
            }

            // Run for 1 tick
            currentProcess.decreaseRemainingTime(1);
            quantumElapsed++;
            
            // 5. Check Completion
            if (currentProcess.isFinished()) {
                currentProcess.setCompletionTime(currentTime + 1); // Finished at END of this tick
                metrics.addCompletedProcess(currentProcess);
                completedCount++;
                currentProcess = null;
                quantumElapsed = 0;
                
                // We just finished. Next tick we will schedule new one.
                // We assume context switch happens on selection.
            } else {
                // 6. Check Preemption
                if (scheduler.shouldPreempt(currentProcess, currentTime, quantumElapsed)) {
                    // Preempt!
                    scheduler.onProcessArrival(currentProcess); // Re-queue
                    currentProcess = null;
                    quantumElapsed = 0;
                    // Next tick loop will pick new process and incur switch cost.
                }
            }

            currentTime++;
        }
        
        metrics.setTotalSimulationTime(currentTime);
    }
}
