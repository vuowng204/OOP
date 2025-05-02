/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.RoomType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {
    private Connection connection;

    public RoomTypeDAO(Connection connection) {
        this.connection = connection;
    }

    public void addRoomType(RoomType type) throws SQLException {
        String sql = "INSERT INTO room_types (typeID, description) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, type.getTypeID());
        stmt.setString(2, type.getDescription());
        stmt.executeUpdate();
    }

    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> types = new ArrayList<>();
        String sql = "SELECT * FROM room_types";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            types.add(new RoomType(
                rs.getString("typeID"),
                rs.getString("description")
            ));
        }
        return types;
    }
}

