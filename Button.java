import java.awt.*;
import javax.swing.*;

public class Button {
    private JLabel label;
    private Table table;
    private String[] scheduling = {"Round Robin", "SRT", "SJN", "Non-Preemptive Priority"}; // Schedules

    public Button(Table tbl) {
        table = tbl;
        JButton buttonAddRow = new JButton("ADD ROW");
        buttonAddRow.setPreferredSize(new Dimension(150,50));
        buttonAddRow.setFocusable(false);
        buttonAddRow.addActionListener(e -> {table.addRow();}); // Adding button actionListener (action will happen whenever u click that button)

        JButton buttonDeleteRow = new JButton("DELETE ROW");
        buttonDeleteRow.setPreferredSize(new Dimension(150,50));
        buttonDeleteRow.setFocusable(false);
        buttonDeleteRow.addActionListener(e -> {table.deleteRow();}); // Deleting button actionListener (action will happen whenever u click that button)

        JComboBox buttonScheduling = new JComboBox<>(scheduling);
        buttonScheduling.setPreferredSize(new Dimension(150,50));
        buttonScheduling.setFocusable(false);
        buttonScheduling.addActionListener(e -> {
            int selectedScheduling = buttonScheduling.getSelectedIndex();

            switch (selectedScheduling) {
                case 0:
                    if(table.updateProcessData())
                        table.RoundRobin();
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "SRT - Dharven", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "SJN - Enoch", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Non-Preemptive Priority - Daniel", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid Scheduling Algorithm Selected");
                    break;
            }

            
        }); // Update button actionListener (action will happen whenever u click that button)

        //Create a label to hold the buttons
        label = new JLabel();
        label.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        label.setBorder(BorderFactory.createEmptyBorder(60,0,0,0));
        label.add(buttonAddRow);
        label.add(buttonDeleteRow);
        label.add(buttonScheduling);

    }

    public JLabel getLabelButton() {
        return label;
    }
}


// Don't forget to add your algorithm in the buttons action listener to work 
// Do your algorithms in the table class due easy access of list 