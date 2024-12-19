import java.awt.*;
import javax.swing.*;

public class Button {
    private JLabel label;
    private Table table;
    private String[] scheduling = {"Round Robin", "SRT", "SJN", "Preemptive Priority"};

    public Button(Table tbl) {
        table = tbl;
        JButton buttonAddRow = new JButton("ADD ROW");
        buttonAddRow.setPreferredSize(new Dimension(150,50));
        buttonAddRow.setFocusable(false);
        buttonAddRow.addActionListener(e -> {table.addRow();});

        JButton buttonDeleteRow = new JButton("DELETE ROW");
        buttonDeleteRow.setPreferredSize(new Dimension(150,50));
        buttonDeleteRow.setFocusable(false);
        buttonDeleteRow.addActionListener(e -> {table.deleteRow();});

        JComboBox buttonScheduling = new JComboBox(scheduling);
        buttonScheduling.setPreferredSize(new Dimension(150,50));
        buttonScheduling.setFocusable(false);
        buttonScheduling.addActionListener(e -> {table.updateProcessData();});

        //Create a label to hold the buttons
        label = new JLabel();
        label.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        label.setBorder(BorderFactory.createEmptyBorder(60,0,0,0));
        label.add(buttonAddRow);
        label.add(buttonDeleteRow);
        label.add(buttonScheduling);

    }

    // Method to get the label
    public JLabel getLabelButton() {
        return label;
    }
}
