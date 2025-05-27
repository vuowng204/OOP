/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import QuanLyKhachSan.dao.RoomDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Room;

public class RoomSelectionDialog extends JDialog {
    private int selectedRoomId = -1;
    private JComboBox<Integer> roomComboBox;

    public RoomSelectionDialog(Frame parent, int currentRoomId) {
        super(parent, "Chọn Phòng Mới", true);
        setLayout(new BorderLayout());
        setSize(300, 150);

        // Tải danh sách phòng trống
        RoomDAO roomDAO = new RoomDAO(JDBCConnection.getConnection());
        List<Room> availableRooms = null;
        try {
            availableRooms = roomDAO.getAvailableRooms();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        roomComboBox = new JComboBox<>();
        if (availableRooms != null) {
            for (Room room : availableRooms) {
                if (room.getId()!= currentRoomId) { // Loại bỏ phòng hiện tại
                    roomComboBox.addItem(room.getId());
                }
            }
        }

        JButton selectButton = new JButton("Chọn");
        selectButton.addActionListener(e -> {
            if (roomComboBox.getSelectedItem() != null) {
                selectedRoomId = (int) roomComboBox.getSelectedItem();
                dispose();
            }
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);

        add(new JLabel("Chọn phòng trống:"), BorderLayout.NORTH);
        add(new JScrollPane(roomComboBox), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    public int getSelectedRoomId() {
        return selectedRoomId;
    }
}