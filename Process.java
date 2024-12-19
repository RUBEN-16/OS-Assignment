public class Process {
    private String name;
    private int burstTime, priority, arrivalTime; // Input
    private int completionTime, turnAroundTime, waitingTime; // Output

    // Constructor
    public Process(String name, int burstTime, int arrivalTime, int priority) {
        this.name = name;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBurstTime() { return burstTime; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }

    public int getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    
    // To check the data in the terminal
    @Override
    public String toString() {
        return String.format("Process: %s, Burst Time: %d, Arrival Time: %d, Priority: %d",name, burstTime, arrivalTime, priority);
    }
}
 