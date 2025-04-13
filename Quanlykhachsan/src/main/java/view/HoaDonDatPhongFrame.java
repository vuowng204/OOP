/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoaDonDatPhongFrame extends JFrame {
    public HoaDonDatPhongFrame() {
        setTitle("Hóa Đơn Đặt Phòng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("HÓA ĐƠN ĐẶT PHÒNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel tìm kiếm (phía trên bảng)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Mục tìm kiếm:"));

        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Tìm kiếm");
        searchPanel.add(searchButton);

        JButton printButton = new JButton("In hóa đơn");
        searchPanel.add(printButton);

        mainPanel.add(searchPanel, BorderLayout.CENTER);

        // Bảng hóa đơn
        String[] columnNames = {"ID Hóa đơn", "ID Nhân viên", "ID Khách hàng", "ID Phòng", "Check-in", "Check-out", "Tiền cọc", "Phụ thu Check-in", "Phụ thu Check-out", "Tổng tiền", "Số người", "Mô hình thuê", "Trạng thái"};
        Object[][] data = {
                {"HD001", "NV01", "KH01", "P101", "2025-04-10", "2025-04-12", "500000", "50000", "70000", "1200000", "2", "Theo giờ", "Đã thanh toán"},
                {"HD002", "NV02", "KH02", "P102", "2025-04-11", "2025-04-13", "600000", "0", "80000", "1300000", "1", "Theo ngày", "Chưa thanh toán"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HoaDonDatPhongFrame::new);
    }
}
