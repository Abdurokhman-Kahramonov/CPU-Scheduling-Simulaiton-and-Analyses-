package org.example.cpusched;

import org.example.cpusched.engine.SimulationEngine;
import org.example.cpusched.metrics.MetricsCollector;
import org.example.cpusched.model.Process;
import org.example.cpusched.scheduler.*;
import org.example.cpusched.workload.WorkloadGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Defaults
        String algorithm = "FCFS";
        int quantum = 4;
        String workloadType = "mixed";
        int processCount = 20;
        int contextSwitch = 0;
        String outputFile = null;

        // Parse Args
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--algorithm":
                    if (i + 1 < args.length) algorithm = args[++i];
                    break;
                case "--quantum":
                    if (i + 1 < args.length) quantum = Integer.parseInt(args[++i]);
                    break;
                case "--workload":
                    if (i + 1 < args.length) workloadType = args[++i];
                    break;
                case "--processes":
                    if (i + 1 < args.length) processCount = Integer.parseInt(args[++i]);
                    break;
                case "--switch":
                    if (i + 1 < args.length) contextSwitch = Integer.parseInt(args[++i]);
                    break;
                case "--out":
                    if (i + 1 < args.length) outputFile = args[++i];
                    break;
                case "--help":
                    printHelp();
                    return;
            }
        }

        System.out.println("Starting Simulation...");
        System.out.println("Algorithm: " + algorithm);
        System.out.println("Workload: " + workloadType + " (" + processCount + " processes)");
        
        // 1. Setup Scheduler
        Scheduler scheduler = createScheduler(algorithm, quantum);
        if (scheduler == null) {
            System.err.println("Unknown algorithm: " + algorithm);
            printHelp();
            return;
        }

        // 2. Generate Workload
        WorkloadGenerator generator = new WorkloadGenerator(12345); // Fixed seed for reproducibility
        List<Process> workload = generator.generateWorkload(workloadType, processCount);

        // 3. Setup Metrics
        MetricsCollector metrics = new MetricsCollector();

        // 4. Run Engine
        SimulationEngine engine = new SimulationEngine(scheduler, workload, metrics, contextSwitch);
        engine.run();

        // 5. Output Results
        metrics.printMetrics(scheduler.getName());
        
        if (outputFile != null) {
            metrics.exportToCsv(outputFile);
        }
    }

    private static Scheduler createScheduler(String name, int quantum) {
        switch (name.toUpperCase()) {
            case "FCFS": return new FCFS();
            case "SJF": return new SJF();
            case "SRTF": return new SRTF();
            case "PRIORITY": return new PriorityScheduler();
            case "RR": return new RoundRobin(quantum);
            case "MLFQ": return new MLFQ();
            default: return null;
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar cpu-sim.jar [options]");
        System.out.println("Options:");
        System.out.println("  --algorithm <name>   Algorithm: FCFS, SJF, SRTF, PRIORITY, RR, MLFQ");
        System.out.println("  --quantum <int>      Time quantum for RR (default: 4)");
        System.out.println("  --workload <type>    Workload: interactive, batch, mixed");
        System.out.println("  --processes <int>    Number of processes (default: 20)");
        System.out.println("  --switch <int>       Context switch cost (default: 0)");
        System.out.println("  --out <file>         Output CSV file");
    }
}
