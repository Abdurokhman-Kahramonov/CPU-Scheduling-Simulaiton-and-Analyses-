package org.example.cpusched.workload;

import org.example.cpusched.model.Process;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkloadGenerator {
    private final Random random;

    public WorkloadGenerator(long seed) {
        this.random = new Random(seed);
    }

    public List<Process> generateWorkload(String type, int count) {
        List<Process> processes = new ArrayList<>();
        int arrivalTime = 0;

        for (int i = 0; i < count; i++) {
            // Poisson arrival approximation (exponential inter-arrival)
            // lambda = 0.5 (mean inter-arrival = 2ms)
            int interArrival = (int) (-Math.log(1.0 - random.nextDouble()) * 2);
            arrivalTime += interArrival;

            int burstTime = 0;
            int priority = random.nextInt(10) + 1; // 1-10

            switch (type.toLowerCase()) {
                case "interactive":
                    // Short bursts: 1-10ms
                    burstTime = random.nextInt(10) + 1;
                    break;
                case "batch":
                    // Long bursts: 20-200ms
                    burstTime = random.nextInt(181) + 20;
                    break;
                case "mixed":
                default:
                    // 70% short, 30% long
                    if (random.nextDouble() < 0.7) {
                        burstTime = random.nextInt(10) + 1;
                    } else {
                        burstTime = random.nextInt(181) + 20;
                    }
                    break;
            }

            processes.add(new Process(i + 1, arrivalTime, burstTime, priority));
        }
        return processes;
    }
}
