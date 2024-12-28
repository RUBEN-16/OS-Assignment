import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Button {
    private JLabel label;
    private Table table;
    private String[] scheduling = {"ROUND ROBIN", "SRT", "SJN", "NON-PREEMPTIVE PRIORITY"}; // Schedules

    public Button(Table tbl) {
        table = tbl;

        // Create the "Add Row" button
        JButton buttonAddRow = createStyledButton("ADD ROW", e -> table.addRow());

        // Create the "Delete Row" button
        JButton buttonDeleteRow = createStyledButton("DELETE ROW", e -> table.deleteRow());

        // Create the scheduling JComboBox
        JComboBox<String> buttonScheduling = new JComboBox<>(scheduling);
        buttonScheduling.setPreferredSize(new Dimension(150, 50));
        buttonScheduling.setFocusable(false);
        buttonScheduling.setFont(new Font("SansSerif", Font.BOLD, 20));
        buttonScheduling.addActionListener(e -> {
            int selectedScheduling = buttonScheduling.getSelectedIndex();
            switch (selectedScheduling) {
                case 0:
                    if (table.updateProcessData())
                        table.RoundRobin();
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "SRT - Dharven", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "SJN - Enoch", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 3:
                if (table.updateProcessData())
                table.NonPreemptivePriority();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid Scheduling Algorithm Selected");
                    break;
            }
        });

        // Style the JComboBox with a consistent look
        buttonScheduling.setBackground(new Color(135, 206, 250));
        buttonScheduling.setForeground(Color.white);
        buttonScheduling.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Create a label to hold the buttons
        label = new JLabel();
        label.setLayout(new GridLayout(6, 1, 0, 20));
        label.setBorder(new EmptyBorder(50, 20, 50, 20));
        label.add(new JLabel());
        label.add(buttonAddRow);
        label.add(buttonDeleteRow);
        label.add(buttonScheduling);
    }

    /**
     * Creates a styled button with hover effects and consistent design.
     */
    private JButton createStyledButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(150, 50));
        button.setBackground(new Color(0x778899)); // Steel 
        button.setForeground(Color.white);
        button.setBorder(BorderFactory.createLineBorder(new Color(240, 248, 255), 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("SansSerif", Font.BOLD, 25));
                button.setBackground(new Color(240, 248, 255)); 
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("SansSerif", Font.BOLD, 20));
                button.setBackground(new Color(0x778899));
                button.setForeground(Color.white);
            }
        });

        return button;
    }
    public JLabel getLabelButton() {
        return label;
    }
}
