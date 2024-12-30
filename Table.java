import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class Table {
    final private JLabel label;
    final private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"PROCESS", "ARRIVAL TIME", "BURST TIME", "PRIORITY"};
    private ArrayList<Process> processes; // List of processes that are entered in the table
    private ArrayList<String> ganttChart; //  List of the processes (P1, P2, P3, etc.) that are allocated CPU time during the schedule
    private ArrayList<String> processTimings; // List of the starting time of each process (act as a parallel array to ganttChart)
    private List<Process> readyProcesses; // List of processes that are ready to run (Ready Queue)
    private int totalProcess; // Total number of process that are entered in the table
    private int lastAT; // Time of a process that arrived at last
    private int firstAT; // Time of a process that arrived at first
    final int minNumberColumns = 3;

    public Table() { // Constructor (will create table)
        tableModel = new DefaultTableModel(columnNames, minNumberColumns);
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Alternate row color
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                } else {
                    c.setBackground(new Color(173, 216, 230)); // Highlight selected row
                }
                return c;
            }
        };

        // Font and Row Height
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(50);

        // Table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.PLAIN, 18));
        tableHeader.setBackground(new Color(135, 206, 250)); // Light Blue
        tableHeader.setForeground(Color.BLACK);

        // Cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer.setVerticalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setShowGrid(true);
        table.setGridColor(new Color(211, 211, 211)); // Light Gray Grid

        // Tooltips
        table.setToolTipText("Enter process details here");
        ToolTipManager.sharedInstance().setInitialDelay(0);

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a label to hold the table
        label = new JLabel();
        label.setLayout(new BorderLayout());
        label.add(scrollPane, BorderLayout.CENTER);
    }

    public JLabel getLabelTable() {
        return label;
    }

    public JTable getTable() { 
        return table;
    }

    public void addRow() { // Add row of the table
        int totalRows = table.getRowCount();
        if (totalRows < 10) {
            tableModel.addRow(new Object[]{"", "", "", ""});
        } else {
            JOptionPane.showMessageDialog(label, "Maximum 10 Processes only can schedule!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteRow() { // Delete row of the table
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(label, "Please select a row to DELETE!", "INFO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int totalRows = table.getRowCount();
            if (totalRows > 3)
                tableModel.removeRow(selectedRow);
            else
                JOptionPane.showMessageDialog(label, "Minimum 3 Processes NEEDED to schedule!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean updateProcessData() { // Function that will store the process into the list by creating Process object
        processes = new ArrayList<>();
        int numberRows = table.getRowCount();
        try {
            for (int i = 0; i < numberRows; i++) {
                int priority = 0; // Priority will be zero in default
                Process process;
                String processName = table.getValueAt(i, 0).toString();
                int arrivalTime = Integer.parseInt(table.getValueAt(i, 1).toString());
                int burstTime = Integer.parseInt(table.getValueAt(i, 2).toString());
                try { // Priority column is optional to input
                    priority = Integer.parseInt(table.getValueAt(i, 3).toString());
                } catch (Exception ex) {}
                process = new Process(processName, arrivalTime, burstTime, priority); // Creating process objects 
                processes.add(process); // Add all the processes into the list
            }
            totalProcess = processes.size();
            return true;
        } catch (Exception e) {
            if (numberRows == 3)
                JOptionPane.showMessageDialog(label, "Input the 3 processes details minimum in the table!", "ERROR", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(label, "Remove the empty row!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void sortAT(){ // sorting ArrayList processes based on their arrival time 
        int iteration = totalProcess-1;
        for(int i = 0; i < totalProcess; i++){
            for(int j = 0; j < iteration; j++){
                if(processes.get(j).getArrivalTime() > processes.get(j+1).getArrivalTime()){
                    Process tempProcess = processes.get(j);
                    processes.set(j, processes.get(j+1));
                    processes.set(j+1, tempProcess);
                }
            }
            iteration--;
        }
        lastAT = processes.get(totalProcess-1).getArrivalTime();
        firstAT = processes.get(0).getArrivalTime();
        // System.out.printf("High AT: %d", lastAT);
        // System.out.printf("First AT: %d", firstAT);
        // System.out.println("\nSorted Processes:  ");
        // for(Process prs: processes){
        //     System.out.println(prs);
        // }
        // System.out.printf("Number of processes: %d", totalProcess);
    }

    private void readyQueuing(int T){ // Function that will queue up the processes that are arrived at T(time)
        for(Process prs: processes){
            if(prs.getArrivalTime() <= T && !readyProcesses.contains(prs) && prs.getBurstTime() > 0){ 
                readyProcesses.add(prs);
                // System.out.println("Added process: " + prs.getName() + " at time " + T);
            }
        }
        // CHECKING PURPOSES
        // System.out.printf("Before:  [");
        // for(Process prs: readyProcesses){
        //     System.out.printf("%s = %d, ", prs.getName(), prs.getBurstTime());
        // }
        // System.out.printf("]%n");
    }

    private void queuePriority(){ // Adding Queue 
        
        readyProcesses.sort((p1, p2) -> {
            if (p1.getArrivalTime() == p2.getArrivalTime()) {
                return Integer.compare( p1.getPriority(), p2.getPriority());
            }
            return 0;
        });
        // CHECKING PURPOSES
        // System.out.printf("After:  [");
        // for(Process prs: readyProcesses){
        //     System.out.printf("%s = %d, ", prs.getName(), prs.getBurstTime());
        // }
        // System.out.printf("]%n");
    }

    // Round Robin Scheduling (Quantum = 3)
    public void RoundRobin(){
        final int quantum = 3;
        int time = 0;
        ganttChart = new ArrayList<>();
        processTimings = new ArrayList<>();
        readyProcesses = new LinkedList<>();
        
        sortAT(); // sorting process in the list in ascending order of AT
        readyQueuing(time); 
        queuePriority(); 

        processTimings.add(String.valueOf(time));
        while(processes.stream().anyMatch(p -> p.getBurstTime() > 0)){
            if(!readyProcesses.isEmpty()){
                Process currentProcess = readyProcesses.get(0);
                ganttChart.add(currentProcess.getName());
                int remainingBurstTime = currentProcess.getBurstTime();
                if(remainingBurstTime <= quantum){
                    time += remainingBurstTime;
                    currentProcess.setBurstTime(0);
                    currentProcess.setCompletionTime(time);
                    currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getInitialBurstTime());
                    readyQueuing(time);
                    readyProcesses.remove(0);
                    queuePriority();
                }else{
                    time += quantum;
                    currentProcess.setBurstTime(remainingBurstTime - quantum);
                    readyQueuing(time);
                    readyProcesses.remove(0);
                    queuePriority();
                    readyProcesses.add(currentProcess);
                }
            }else{
                ganttChart.add("IDLE");
                time++;
                readyQueuing(time); 
                queuePriority(); 
            }
            processTimings.add(String.valueOf(time));
        }
        // for(String t: processTimings){
        //     System.out.printf("%s ", t);
        // }
        displayResults();
        
        // // Display Gantt Chart in terminal (use when u want to check)
        // System.out.println("");
        // System.out.println(String.join(" | ", ganttChart));

        // // Display process table with updated TAT, CT and WT
        // System.out.println("\nProcess Details:");
        // System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%n", "Process", "Arrival Time", "Burst Time", "Completion Time", "TAT", "WT");
        // for (Process p : processes) {
        //     System.out.printf("%-10s%-15d%-15d%-15d%-15d%-15d%n", 
        //     p.getName(), p.getArrivalTime(), p.getInitialBurstTime(), p.getCompletionTime(), 
        //     p.getTurnAroundTime(), p.getWaitingTime());
        // }
        
    }
    
    public void SJN() {
        int time = 0;
        ganttChart = new ArrayList<>();
        processTimings = new ArrayList<>();
        readyProcesses = new LinkedList<>();

        sortAT(); // Sort processes by arrival time
        readyQueuing(time); // Queue up the initial processes

        processTimings.add(String.valueOf(time));
        while (processes.stream().anyMatch(p -> p.getBurstTime() > 0)) {
            if (!readyProcesses.isEmpty()) {
                // Select the process with the shortest burst time in the ready queue
                readyProcesses.sort((p1, p2) -> {
                    if (p1.getBurstTime() == p2.getBurstTime()) {
                        return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
                    }
                    return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
                });

                Process currentProcess = readyProcesses.remove(0); // Dequeue the selected process
                ganttChart.add(currentProcess.getName());
                time += currentProcess.getBurstTime(); // Process runs to completion
                currentProcess.setCompletionTime(time);
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess
                        .setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getInitialBurstTime());
                currentProcess.setBurstTime(0); // Mark process as completed

                readyQueuing(time); // Check for new arrivals at the updated time
            } else {
                ganttChart.add("IDLE");
                time++; // CPU is idle
                readyQueuing(time); // Check for new arrivals
            }
            processTimings.add(String.valueOf(time));
        }
        displayResults();
    }
    
    public void NonPreemptivePriority() {
        int time = 0;
        ganttChart = new ArrayList<>();
        processTimings = new ArrayList<>();
        readyProcesses = new LinkedList<>();
    
        sortAT(); // Sort processes by arrival time
        readyQueuing(time); // Queue up the initial processes
    
        processTimings.add(String.valueOf(time));
        while (processes.stream().anyMatch(p -> p.getBurstTime() > 0)) {
            if (!readyProcesses.isEmpty()) {
                // Select the process with the highest priority in the ready queue
                readyProcesses.sort((p1, p2) -> {
                    if (p1.getPriority() == p2.getPriority()) {
                        return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
                    }
                    return Integer.compare(p1.getPriority(), p2.getPriority());
                });
    
                Process currentProcess = readyProcesses.remove(0); // Dequeue the selected process
                ganttChart.add(currentProcess.getName());
                time += currentProcess.getBurstTime(); // Process runs to completion
                currentProcess.setCompletionTime(time);
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getInitialBurstTime());
                currentProcess.setBurstTime(0); // Mark process as completed
    
                readyQueuing(time); // Check for new arrivals at the updated time
            } else {
                ganttChart.add("IDLE");
                time++; // CPU is idle
                readyQueuing(time); // Check for new arrivals
            }
            processTimings.add(String.valueOf(time));
        }
        displayResults();
    }
    

    public void displayResults() { // Will display the Gantt chart and table in the GUI
        // Create a JFrame to display the results
        ImageIcon img = new ImageIcon("CPU.png");
        JFrame resultFrame = new JFrame("Scheduling Results");
        resultFrame.setSize(1500, 700);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setLayout(new BorderLayout());
        resultFrame.setIconImage(img.getImage());
        resultFrame.setLocationRelativeTo(null);
    
        // Panel for Gantt Chart
        JPanel ganttPanel = new JPanel();
        ganttPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Gantt Chart"),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        ganttPanel.setLayout(new BoxLayout(ganttPanel, BoxLayout.Y_AXIS));
    
        // Sub-panel for process blocks
        JPanel processPanel = new JPanel();
        processPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    
        // Sub-panel for timings
        JPanel timingPanel = new JPanel();
        timingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timingPanel.setPreferredSize(new Dimension(0, 40));
    
        // Generate the Gantt Chart
        JLabel emptyLabelStart = new JLabel();
        emptyLabelStart.setPreferredSize(new Dimension(5, 30));
        processPanel.add(emptyLabelStart);
    
        int currentTime = 0; // Start time for the first block
        for (String processName : ganttChart) {
            int processedTime = Integer.parseInt(processTimings.get(currentTime + 1)) - Integer.parseInt(processTimings.get(currentTime));
    
            // Create label for process block
            JLabel processLabel = new JLabel(processName);
            processLabel.setOpaque(true);
            processLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 0, 0, 0)
            ));
            processLabel.setHorizontalAlignment(SwingConstants.CENTER);
            processLabel.setFont(new Font("Consolas", Font.PLAIN, 20));
    
            // Create label for timing below the block
            JLabel timeLabel = new JLabel(processTimings.get(currentTime));
            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            timeLabel.setFont(new Font("Consolas", Font.PLAIN, 15));
    
            if (processName.equals("IDLE")) {
                processLabel.setPreferredSize(new Dimension(80 * processedTime, 40));
                processLabel.setBackground(Color.lightGray);
                timeLabel.setPreferredSize(new Dimension(80 * processedTime, 30));
            } else {
                processLabel.setPreferredSize(new Dimension(50 * processedTime, 40));
                processLabel.setBackground(new Color(135, 206, 250));
                timeLabel.setPreferredSize(new Dimension(50 * processedTime, 30));
            }
    
            processPanel.add(processLabel);
            timingPanel.add(timeLabel);
    
            // Update the current time (assuming each block corresponds to 1 time unit)
            currentTime++; // Replace with actual duration of the block if needed
        }
        JLabel emptyLabelEnd = new JLabel();
        emptyLabelEnd.setPreferredSize(new Dimension(60, 30));
        processPanel.add(emptyLabelEnd);
    
        // Add the final time
        JLabel finalTimeLabel = new JLabel(processTimings.get(currentTime));
        finalTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        finalTimeLabel.setPreferredSize(new Dimension(60, 20));
        finalTimeLabel.setFont(new Font("Consolas", Font.PLAIN, 15));
        timingPanel.add(finalTimeLabel);
    
        // Add sub-panels to Gantt Chart panel
        ganttPanel.add(processPanel);
        ganttPanel.add(timingPanel);
    
        // Add Gantt Chart panel to a JScrollPane
        JScrollPane ganttScrollPane = new JScrollPane(ganttPanel);
        ganttScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ganttScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    
        // Panel for process table
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Process Details"));
        tablePanel.setLayout(new BorderLayout());
    
        // Create a JTable for CT, WT, and TAT
        String[] tableColumns = {"Process", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time"};
        Object[][] tableData = new Object[processes.size() + 2][6]; // Add two extra rows for totals and averages
    
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
    
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            tableData[i][0] = p.getName();
            tableData[i][1] = p.getArrivalTime();
            tableData[i][2] = p.getInitialBurstTime();
            tableData[i][3] = p.getCompletionTime();
            tableData[i][4] = p.getTurnAroundTime();
            tableData[i][5] = p.getWaitingTime();
    
            totalTurnaroundTime += p.getTurnAroundTime();
            totalWaitingTime += p.getWaitingTime();
        }
        
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double averageWaitingTime = (double) totalWaitingTime / processes.size();

        // Add Total row
        tableData[processes.size()][0] = "Total";
        tableData[processes.size()][1] = "-";
        tableData[processes.size()][2] = "-";
        tableData[processes.size()][3] = "-";
        tableData[processes.size()][4] = totalTurnaroundTime;
        tableData[processes.size()][5] = totalWaitingTime;
    
        // Add Average row
        tableData[processes.size() + 1][0] = "Average";
        tableData[processes.size() + 1][1] = "-";
        tableData[processes.size() + 1][2] = "-";
        tableData[processes.size() + 1][3] = "-";
        tableData[processes.size() + 1][4] = String.format("%.2f ms", averageTurnaroundTime);
        tableData[processes.size() + 1][5] = String.format("%.2f ms", averageWaitingTime);
    
        // Create a custom table model to make cells non-editable
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };
    
        JTable resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Consolas", Font.PLAIN, 16));
        resultTable.setRowHeight(30);
    
        // Center-align the table data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
    
        // Add components to the frame
        resultFrame.add(ganttScrollPane, BorderLayout.NORTH);
        resultFrame.add(tablePanel, BorderLayout.CENTER);
    
        // Display the frame
        resultFrame.setVisible(true);
    }
    
    
    
}
