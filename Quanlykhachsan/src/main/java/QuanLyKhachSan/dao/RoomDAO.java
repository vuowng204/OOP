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
import java.util.List;
import java.util.ArrayList;

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
    // Thêm phòng

    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (id, ten_phong, loai_phong, gia_phong, tang, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, room.getId());
        stmt.setString(2, room.getTenPhong());
        stmt.setString(3, room.getLoaiphong().getTypeID());
        stmt.setDouble(4, room.getGiaPhong());
        stmt.setString(5, room.getTang());
        stmt.setString(6, room.getTrangThai());
        stmt.executeUpdate();
    }

// Cập nhật phòng
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET ten_phong=?, loai_phong=?, gia_phong=?, tang=?, trang_thai=? WHERE id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, room.getTenPhong());
        stmt.setString(2, room.getLoaiphong().getTypeID());
        stmt.setDouble(3, room.getGiaPhong());
        stmt.setString(4, room.getTang());
        stmt.setString(5, room.getTrangThai());
        stmt.setInt(6, room.getId());
        stmt.executeUpdate();
    }

// Xóa phòng
    public void deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, roomId);
        stmt.executeUpdate();
    }

// Tìm kiếm phòng theo tên
    public List<Room> searchRoomByName(String keyword) throws SQLException {
        List<Room> result = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE ten_phong LIKE ? ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + keyword + "%");
      
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setTenPhong(rs.getString("ten_phong"));
            room.setGiaPhong(rs.getDouble("gia_phong"));
            room.setTang(rs.getString("tang"));
            room.setTrangThai(rs.getString("trang_thai"));
            room.setLoaiphong(new RoomType(rs.getString("loai_phong"), ""));
            result.add(room);
        }
        return result;
    }
        public List<Room> searchRoomByID(String keyword) throws SQLException {
        List<Room> result = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE id LIKE ? ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + keyword + "%");
      
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setTenPhong(rs.getString("ten_phong"));
            room.setGiaPhong(rs.getDouble("gia_phong"));
            room.setTang(rs.getString("tang"));
            room.setTrangThai(rs.getString("trang_thai"));
            room.setLoaiphong(new RoomType(rs.getString("loai_phong"), ""));
            result.add(room);
        }
        return result;
    }
    public Room getRoomByIdWithTypeInfo(int id) throws SQLException {
        String sql = "SELECT r.*, rt.description FROM rooms r "
                + "LEFT JOIN room_types rt ON r.loai_phong = rt.typeID "
                + "WHERE r.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setTenPhong(rs.getString("ten_phong"));
                room.setGiaPhong(rs.getDouble("gia_phong"));
                room.setTang(rs.getString("tang"));
                room.setTrangThai(rs.getString("trang_thai"));

                String typeID = rs.getString("loai_phong");
                String description = rs.getString("description") != null ? rs.getString("description") : "";

                RoomType roomType = new RoomType(typeID, description);
                room.setLoaiphong(roomType);

                return room;
            }
        }

        return null;
    }
    public List<Room> getAvailableRooms() throws SQLException {
    List<Room> rooms = new ArrayList<>();
    String sql = "SELECT * FROM rooms WHERE trang_thai = 'Trống'";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setTenPhong(rs.getString("ten_phong"));
            room.setGiaPhong(rs.getDouble("gia_phong"));
            room.setTrangThai(rs.getString("trang_thai"));
            rooms.add(room);
        }
    }
    return rooms;
}
}
