public class Process {
    private String name;
    private int burstTime, priority, arrivalTime, initialBurstTime; // Input
    private int completionTime, turnAroundTime, waitingTime; // Output

    // Constructor
    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.initialBurstTime = burstTime;
    }
    
    // Getters and setters
    public int getInitialBurstTime() { 
        return initialBurstTime; 
    }

    public String getName() { 
        return name; 
    }
    
    public int getPriority() { 
        return priority; 
    }

    public int getArrivalTime() { 
        return arrivalTime; 
    } 

    public int getCompletionTime() { 
        return completionTime; 
    }
    public void setCompletionTime(int completionTime) { 
        this.completionTime = completionTime; 
    }

    public int getTurnAroundTime() { 
        return turnAroundTime; 
    }
    public void setTurnAroundTime(int turnAroundTime) { 
        this.turnAroundTime = turnAroundTime; 
    }

    public int getWaitingTime() { 
        return waitingTime; 
    }
    public void setWaitingTime(int waitingTime) { 
        this.waitingTime = waitingTime; 
    }

    public int getBurstTime() { 
        return burstTime; 
    }
    public void setBurstTime(int burstTime) { 
        this.burstTime = burstTime; 
    }
    
    // To check the data in the terminal
    @Override
    public String toString() {
        return String.format("Process: %s, Arrival Time: %d, Burst Time: %d, Priority: %d",name, arrivalTime, burstTime, priority);
    }
}
 