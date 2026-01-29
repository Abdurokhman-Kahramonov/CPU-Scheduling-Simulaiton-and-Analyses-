# CPU Scheduling Algorithms Simulator (Java)

A **discrete‑event CPU scheduling simulator** written in **Java 17+**, designed for academic research and operating‑systems coursework. The project evaluates classical and modern CPU scheduling algorithms under identical workloads and produces **quantitative performance metrics** suitable for tables, graphs, and formal analysis.

This simulator was designed with **clean OOP architecture**, reproducibility, and extensibility in mind.

---

## ✨ Features

- Discrete‑time simulation (1 ms resolution)
- Single‑core CPU model (multi‑core extension ready)
- Modular scheduling architecture (Strategy pattern)
- Deterministic results via fixed random seed
- CSV export for external analysis (Excel, Python, R)
- Suitable for **research papers, lab work, and performance evaluation**

---

## 📌 Implemented Scheduling Algorithms

| Algorithm | Type | Notes |
|--------|------|------|
| FCFS | Non‑preemptive | Minimal overhead, convoy effect |
| SJF | Non‑preemptive | Optimal avg waiting time (oracle) |
| SRTF | Preemptive | Minimizes mean waiting time |
| Priority Scheduling | Preemptive | Priority inheritance supported |
| Round Robin (RR) | Preemptive | Configurable time quantum |
| Multilevel Feedback Queue (MLFQ) | Hybrid | Aging + demotion across queues |

---

## 🧠 Simulation Model

- **CPU:** single‑core
- **Time unit:** milliseconds
- **Context‑switch cost:** configurable (default `0.2 ms`)
- **Scheduling resolution:** 1 ms

### Process Attributes
Each process includes:

- PID
- Arrival time
- Burst time
- Remaining time
- Priority
- First scheduled time (response)
- Completion time
- Total waiting time

---

## 📊 Collected Metrics

For each algorithm, the simulator computes:

- Average Waiting Time
- Average Turnaround Time
- Average Response Time
- 95th & 99th percentile Response Time
- Throughput (jobs/sec)
- CPU Utilization (%)
- Total Context Switches
- Starvation count (waiting > threshold)

All metrics are exported to **CSV** for reproducibility.

---

## 🧪 Workload Generation

Supported workload models:

- **Interactive:** many short CPU bursts
- **Batch:** few long CPU‑bound jobs
- **Mixed:** exponential / Weibull burst distributions

Arrival times follow **Poisson distribution** by default.

---

## 🏗️ Project Structure

```
src/
 ├── model/
 │   └── Process.java
 ├── scheduler/
 │   ├── Scheduler.java
 │   ├── FCFSScheduler.java
 │   ├── SJFScheduler.java
 │   ├── SRTFScheduler.java
 │   ├── PriorityScheduler.java
 │   ├── RoundRobinScheduler.java
 │   └── MLFQScheduler.java
 ├── simulation/
 │   ├── SimulationEngine.java
 │   ├── EventLoop.java
 │   └── MetricsCollector.java
 ├── workload/
 │   └── WorkloadGenerator.java
 └── Main.java
```

---

## ▶️ How to Run

### Compile
```bash
javac -d out src/**/*.java
```

### Run
```bash
java -cp out Main \
  --algorithm RR \
  --quantum 10 \
  --workload interactive \
  --processes 100
```

### Example Algorithms
- `FCFS`
- `SJF`
- `SRTF`
- `PRIORITY`
- `RR`
- `MLFQ`

---

## 📁 Output Files

After execution, the simulator generates:

```
output/
 ├── per_process_results.csv
 ├── summary_metrics.csv
 └── simulation_log.txt
```

These files can be directly imported into:
- Excel / Google Sheets
- Python (pandas, matplotlib)
- R / MATLAB

---

## 📈 Recommended Analysis

- Compare **mean vs tail latency** (95th / 99th percentile)
- Evaluate sensitivity to RR time quantum
- Measure overhead of context switches
- Observe starvation behavior in priority‑based schedulers

---

## 🔧 Design Principles

- Strategy pattern for schedulers
- SOLID‑compliant architecture
- No hard‑coded policies
- Separation of concerns (simulation vs metrics vs workload)

Each scheduler documents:
- Scheduling complexity
- Data structures used
- Trade‑offs and limitations

---

## 🚀 Future Extensions

- Multi‑core scheduling
- NUMA‑aware placement
- Cache‑affinity modeling
- EDF / Rate‑Monotonic scheduling
- GUI visualization (JavaFX)

---

## 📚 Academic References

- Silberschatz, Galvin, Gagne — *Operating System Concepts*
- Tanenbaum — *Modern Operating Systems*
- Arpaci‑Dusseau — *Operating Systems: Three Easy Pieces*
- Liu & Layland — *Scheduling Algorithms for Hard Real‑Time Systems*

---

## 📝 License

This project is intended for **educational and research use**.

You are free to modify, extend, and cite it in academic work.

---

## 👤 Author

**Abdurokhman Kahramonov**  
Software Engineer | Operating Systems & Performance Analysis

---

If you use this simulator in coursework or research, please reference it appropriately.

