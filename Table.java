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
    
    final int numberColumns = 3;
    public Table() {
        tableModel = new DefaultTableModel(columnNames, numberColumns);
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
                int burstTime = Integer.parseInt(table.getValueAt(i, 1).toString());
                int arrivalTime = Integer.parseInt(table.getValueAt(i, 2).toString());
                try { // Priority is optional to input
                    priority = Integer.parseInt(table.getValueAt(i, 3).toString());
                }catch(Exception ex){}
                process = new Process(processName, burstTime, arrivalTime, priority); // Creating process objects 
                processes.add(process); // Add all the processes into the list
            }
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
    
    public JLabel getLabelTable() {
        return label;
    }
    public JTable getTable() {
        return table;
    }

    public ArrayList<Process> getList() {
        return processes;
    }

    // public void getData(){
    //     int numberRows = table.getRowCount();
    //     for(int i = 0; i < numberRows; i++){
    //         Process process = new Process(null, i, i, i);
    //     }

    // }
}





