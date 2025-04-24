/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import QuanLyKhachSan.dao.RoomDAO;
import com.mycompany.quanlykhachsan.model.Room;
import javax.swing.JOptionPane;
import view.Home;
import view.ServiceNRoom;

/**
 *
 * @author Admin
 */
public class RoomController {
    private RoomDAO roomDAO;

    public RoomController(RoomDAO dao) {
        this.roomDAO = dao;
    }

    public void openRoomFrame(Home home, int roomId) {
        Room room = roomDAO.getRoomById(roomId);
        if (room != null) {
            ServiceNRoom sr = new ServiceNRoom(home, roomId, room);
            sr.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(home, "Không tìm thấy phòng!");
        }
    }
}
