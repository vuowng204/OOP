package com.mycompany.quanlykhachsan.model;
import javax.swing.*;

public class DashboardWindow {
    private JFrame frame;

    public DashboardWindow(User user) {
        frame = new JFrame("Chào " + user.getFullname());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Xin chào " + user.getFullname() + " - Vai trò: " + user.getRole());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(label);

        frame.setVisible(true);
    }
}
