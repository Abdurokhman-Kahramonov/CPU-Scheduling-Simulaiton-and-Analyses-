package org.example.cpusched.metrics;

import org.example.cpusched.model.Process;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;

public class MetricsCollector {
    private final List<Process> completedProcesses = new ArrayList<>();
    private int totalContextSwitches = 0;
    private int totalSimulationTime = 0;
    private int totalIdleTime = 0; // CPU idle time

    public void addCompletedProcess(Process p) {
        completedProcesses.add(p);
    }

    public void incrementContextSwitches() {
        totalContextSwitches++;
    }

    public void setTotalSimulationTime(int time) {
        this.totalSimulationTime = time;
    }
    
    public void addIdleTime(int time) {
        this.totalIdleTime += time;
    }

    public void printMetrics(String algorithmName) {
        if (completedProcesses.isEmpty()) {
            System.out.println("No processes completed.");
            return;
        }

        double avgWait = completedProcesses.stream().mapToInt(Process::getTotalWaitingTime).average().orElse(0);
        double avgTurnaround = completedProcesses.stream().mapToInt(Process::getTurnaroundTime).average().orElse(0);
        double avgResponse = completedProcesses.stream().mapToInt(Process::getResponseTime).average().orElse(0);
        
        // Percentiles
        List<Integer> responseTimes = completedProcesses.stream().map(Process::getResponseTime).sorted().toList();
        double p95 = responseTimes.get((int)(responseTimes.size() * 0.95));
        double p99 = responseTimes.get((int)(responseTimes.size() * 0.99));

        double throughput = (double) completedProcesses.size() / totalSimulationTime * 1000.0; // Processes per sec (assuming 1ms tick)
        double cpuUtil = ((double)(totalSimulationTime - totalIdleTime) / totalSimulationTime) * 100.0;

        System.out.println("==========================================");
        System.out.println("Algorithm: " + algorithmName);
        System.out.println("------------------------------------------");
        System.out.printf("Average Waiting Time:    %.2f ms%n", avgWait);
        System.out.printf("Average Turnaround Time: %.2f ms%n", avgTurnaround);
        System.out.printf("Average Response Time:   %.2f ms%n", avgResponse);
        System.out.printf("95th %% Response Time:    %.2f ms%n", p95);
        System.out.printf("99th %% Response Time:    %.2f ms%n", p99);
        System.out.printf("Throughput:              %.2f processes/sec%n", throughput);
        System.out.printf("CPU Utilization:         %.2f%%%n", cpuUtil);
        System.out.printf("Context Switches:        %d%n", totalContextSwitches);
        System.out.println("==========================================");
    }

    public void exportToCsv(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("PID,Arrival,Burst,Priority,Start,Completion,Turnaround,Waiting,Response");
            for (Process p : completedProcesses) {
                writer.printf("%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                    p.getPid(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(),
                    p.getStartTime(), p.getCompletionTime(),
                    p.getTurnaroundTime(), p.getTotalWaitingTime(), p.getResponseTime());
            }
            System.out.println("Detailed metrics exported to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}
