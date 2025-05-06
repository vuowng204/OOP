/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import QuanLyKhachSan.dao.RoomDAO;
import QuanLyKhachSan.dao.UserDAO;
import com.mycompany.quanlykhachsan.model.Room;
import com.mycompany.quanlykhachsan.model.User;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import view.Home;
import view.ServiceNRoom;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Admin
 */
public class RoomController {

    private RoomDAO roomDAO;
    private UserDAO userDAO;
    private User currentUser;

    public RoomController(RoomDAO roomDAO, User currentUser) {
        this.roomDAO = roomDAO;
        this.currentUser = currentUser;
    }

    public void openRoomFrame(Home home, int roomId) throws SQLException {
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            throw new SQLException("Không tìm thấy phòng với ID: " + roomId);
        }
        ServiceNRoom serviceNRoom = new ServiceNRoom(home, roomId, room, currentUser);
        serviceNRoom.setVisible(true);
        serviceNRoom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void addRoom(Room room) {
        try {
            roomDAO.addRoom(room);
            JOptionPane.showMessageDialog(null, "Thêm phòng thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    public void updateRoom(Room room) {
        try {
            roomDAO.updateRoom(room);
            JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    public void deleteRoom(int id) {
        try {
            roomDAO.deleteRoom(id);
            JOptionPane.showMessageDialog(null, "Đã xóa phòng!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + e.getMessage());
        }
    }

    public void searchRoomByName(String keyword, DefaultTableModel model) {
        try {
            List<Room> list = roomDAO.searchRoomByName(keyword);
            model.setRowCount(0);
            for (Room r : list) {
                model.addRow(new Object[]{
                    r.getId(), r.getTenPhong(), r.getLoaiphong().getTypeID(),r.getGiaPhong(), r.getTang(), r.getTrangThai()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

}
