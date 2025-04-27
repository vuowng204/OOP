/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

/**
 *
 * @author Admin
 */
import com.mycompany.quanlykhachsan.model.Room;
import com.mycompany.quanlykhachsan.model.RoomType;
import java.sql.*;

public class RoomDAO {
    private Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setTenPhong(rs.getString("ten_phong"));
                String loaiPhong = rs.getString("loai_phong");
                if (loaiPhong != null) {
                    room.setLoaiphong(new RoomType(loaiPhong, ""));
                }
                room.setGiaPhong(rs.getDouble("gia_phong"));
                room.setTang(rs.getString("tang"));
                room.setTrangThai(rs.getString("trang_thai"));
                return room;
            }
        }
        return null;
    }

    public void updateRoomStatus(int roomId, String status) throws SQLException {
        String sql = "UPDATE rooms SET trang_thai = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
        }
    }

}

