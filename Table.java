import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;


// Basically we have to display the completion time, waiting time and turnaround time in form  of table and also Gantt chart 


public class Table {
    private JLabel label;
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"PROCESS", "ARRIVAL TIME", "BURST TIME", "PRIORITY"};
    private ArrayList<Process> processes;
    private Queue<Process> readyProcesses;
    private int totalProcess, lastAT, firstAT;
    final int minNumberColumns = 3;
    
    public Table() {
        tableModel = new DefaultTableModel(columnNames, minNumberColumns);
        table = new JTable(tableModel);
        table.setFont(new Font("Consolas", Font.PLAIN, 16) );
        table.setRowHeight(30);
        // Table header 
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Consolas", Font.BOLD, 20));
        // Cells
        DefaultTableCellRenderer cells = new DefaultTableCellRenderer();
        cells.setHorizontalAlignment(SwingConstants.CENTER);
        cells.setVerticalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cells);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY); 
        table.setSelectionForeground(Color.black); 

        JScrollPane scrollPane = new JScrollPane(table);

        // Create a label to hold the table
        label = new JLabel();
        label.setLayout(new BorderLayout());
        label.add(scrollPane, BorderLayout.CENTER);
    }

    public void addRow(){
        int totalRows = table.getRowCount();
            if(totalRows < 10){
                tableModel.addRow(new Object[]{"", "", "", ""});
            }else
                JOptionPane.showMessageDialog(label, "Maximum 10 Processes only can schedule!", "ERROR", JOptionPane.ERROR_MESSAGE);
        
    }

    public void deleteRow(){
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(label, "Please select a row to DELETE!", "INFO", JOptionPane.INFORMATION_MESSAGE);
        }else{
            int totalRows = table.getRowCount();
            if(totalRows > 3)
                tableModel.removeRow(selectedRow);
            else
                JOptionPane.showMessageDialog(label, "Minimum 3 Processes NEEDED to schedule!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    // this function will be execute whenever u clicking the scheduling button 
    public void updateProcessData(){
        processes = new ArrayList<Process>();
        int numberRows = table.getRowCount();
        try {
            for(int i = 0 ; i < numberRows; i++){
                int priority = 0;
                Process process;
                String processName = table.getValueAt(i, 0).toString();
                int arrivalTime = Integer.parseInt(table.getValueAt(i, 1).toString());
                int burstTime = Integer.parseInt(table.getValueAt(i, 2).toString());
                try { // Priority is optional to input
                    priority = Integer.parseInt(table.getValueAt(i, 3).toString());
                }catch(Exception ex){}
                process = new Process(processName, arrivalTime, burstTime, priority); // Creating process objects 
                processes.add(process); // Add all the processes into the list
            }
            totalProcess = processes.size();
            for (Process p : processes){
                System.out.println(p);
            }
        } catch (Exception e){
            if(numberRows == 3)
                JOptionPane.showMessageDialog(label, "Input the 3 processes details minimum in the table!", "ERROR", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(label, "Remove the empty row!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
            
    }
    public JLabel getLabelTable() {return label;}
    public JTable getTable() {return table;}
    public ArrayList<Process> getList() {return processes;}

    public void sortAT(){ // sorting Array processes based on their arrival time
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
        System.out.printf("High AT: %d", lastAT);
        System.out.printf("First AT: %d", firstAT);
        System.out.println("\nSorted Processes:  ");
        for(Process prs: processes){
            System.out.println(prs);
        }
        System.out.printf("Number of processes: %d", totalProcess);
    }

    private void readyQueuing(int T){
        for(Process prs: processes){
            if(prs.getArrivalTime() <= T && !readyProcesses.contains(prs) && prs.getBurstTime() > 0){
                readyProcesses.add(prs);
                // System.out.println("Added process: " + prs.getName() + " at time " + T);
            }
        }
    }

    public void RoundRobin(){
        final int quantum = 3;
        int time = 0;
        sortAT(); // sorting process in the list in ascending order of AT
        ArrayList<String> ganttChart = new ArrayList<>();
        readyProcesses = new LinkedList<>();
        System.out.printf("%n      %d ", time);
        while(processes.stream().anyMatch(p -> p.getBurstTime() > 0)){
            readyQueuing(time);  
            Process currentProcess = readyProcesses.peek();
            if(currentProcess != null){
                ganttChart.add(currentProcess.getName());
                int remainingBurstTime = currentProcess.getBurstTime();
                if(remainingBurstTime <= quantum){
                    time += remainingBurstTime;
                    currentProcess.setBurstTime(0);
                    readyQueuing(time);
                    currentProcess.setCompletionTime(time);
                    currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getInitialBurstTime());
                    readyProcesses.poll();
                }else{
                    time += quantum;
                    currentProcess.setBurstTime(remainingBurstTime - quantum);
                    readyQueuing(time);
                    readyProcesses.poll();
                    readyProcesses.add(currentProcess);
                }
            }else{
                ganttChart.add("IDLE");
                time++;
            }
            System.out.printf("%d ", time);
        }

        // Display Gantt Chart
        System.out.println("");
        System.out.println(String.join(" | ", ganttChart));

        // Display process table with updated metrics
        System.out.println("\nProcess Details:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%n", "Process", "Arrival Time", "Burst Time", "Completion Time", "TAT", "WT");
        for (Process p : processes) {
            System.out.printf("%-10s%-15d%-15d%-15d%-15d%-15d%n", p.getName(), p.getArrivalTime(), p.getInitialBurstTime(), 
                    p.getCompletionTime(), p.getTurnAroundTime(), p.getWaitingTime());
        }
        
    }
}





















// public void RoundRobin() {
//     final int quantum = 3;
//     int time = 0;
//     sortAT(); // Sort processes based on arrival time

//     readyQueuing(time);
//     ArrayList<String> ganttChart = new ArrayList<>();

//     while (!readyProcesses.isEmpty() || processes.stream().anyMatch(p -> p.getBurstTime() > 0)) {
//         readyQueuing(time);
//         Process currentProcess = readyProcesses.poll();

//         if (currentProcess != null) {
//             ganttChart.add(currentProcess.getName());
//             int remainingBurstTime = currentProcess.getBurstTime();
//             if (remainingBurstTime <= quantum) {
//                 time += remainingBurstTime;
//                 currentProcess.setBurstTime(0); // Process completed
//                 currentProcess.setCompletionTime(time);
//                 currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
//                 currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getInitialBurstTime());
//             } else {
//                 time += quantum;
//                 currentProcess.setBurstTime(remainingBurstTime - quantum);
//                 readyQueuing(time); // Check if new processes arrived
//                 readyProcesses.add(currentProcess); // Re-queue the current process
//             }
//         } else {
//             ganttChart.add("Idle");
//             time++; // Increment time when idle
//         }
//     }

//     // Display Gantt Chart
//     System.out.println("Gantt Chart:");
//     System.out.println(String.join(" | ", ganttChart));

//     // Display process table with updated metrics
//     System.out.println("\nProcess Details:");
//     System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%n", "Process", "Arrival Time", "Burst Time", "Completion Time", "TAT", "WT");
//     for (Process p : processes) {
//         System.out.printf("%-10s%-15d%-15d%-15d%-15d%-15d%n", p.getName(), p.getArrivalTime(), p.getInitialBurstTime(), 
//                 p.getCompletionTime(), p.getTurnAroundTime(), p.getWaitingTime());
//     }
// }
