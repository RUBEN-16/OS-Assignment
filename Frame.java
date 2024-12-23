import java.awt.*;
import javax.swing.*;

public class Frame {
    private Table table;

    public Frame() {
        ImageIcon img = new ImageIcon("CPU.png");

        // Create the main frame
        JFrame frame = new JFrame("CPU Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 700);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(img.getImage());
        frame.setResizable(true);

        // Gradient panel for the main background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180); // Steel Blue
                Color color2 = new Color(240, 248, 255); // Alice Blue
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Table section
        JLabel labelTable = new JLabel();
        labelTable.setOpaque(true);
        labelTable.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white
        labelTable.setPreferredSize(new Dimension(1100, 0));
        labelTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        table = new Table();
        labelTable.setLayout(new BorderLayout());
        labelTable.add(table.getLabelTable(), BorderLayout.CENTER);

        // Button section
        JLabel labelButtons = new JLabel();
        labelButtons.setOpaque(false);
        labelButtons.setLayout(new BorderLayout());
        labelButtons.setPreferredSize(new Dimension(300, 0));
        labelButtons.setVerticalAlignment(JLabel.CENTER);

        Button btn = new Button(table);
        JLabel buttonPanel = btn.getLabelButton();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setOpaque(false);

        labelButtons.add(buttonPanel, BorderLayout.CENTER);

        // Panel with table and buttons
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(labelTable, BorderLayout.WEST);
        contentPanel.add(labelButtons, BorderLayout.EAST);

        // Add content panel to the frame
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(backgroundPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
