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
}


