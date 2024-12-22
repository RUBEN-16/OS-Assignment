import java.awt.*;
import javax.swing.*;

public class Frame {
    private String process;
    private int burstTime, arrivalTime, priority;
    private Table table;

    public Frame(){
        ImageIcon img = new ImageIcon("CPU.png");

        JFrame frame = new JFrame("CPU Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,438);
        frame.setIconImage(img.getImage());
        frame.setResizable(false);

        // LABEL => Table
        JLabel label0 = new JLabel(); 
        label0.setOpaque(true); 
        label0.setBackground(Color.white);
        label0.setPreferredSize(new Dimension(900, 0));
        label0.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        table = new Table();
        label0.setLayout(new BorderLayout());
        label0.add(table.getLabelTable(), BorderLayout.CENTER); // Add the created table to label0
        // LABEL => Button
        JLabel label = new JLabel();        
        label.setOpaque(true); 
        label.setBackground(Color.white);
        label.setLayout(new BorderLayout());
        label.setPreferredSize(new Dimension(0, 0));
        label.setVerticalAlignment(JLabel.CENTER);

        Button btn = new Button(table);
        label.add(btn.getLabelButton());
        label.add(label0, BorderLayout.WEST);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setBackground(Color.white);
        panel1.setPreferredSize(new Dimension(0, 400));
        panel1.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel1.add(label);
        
        frame.add(panel1);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
