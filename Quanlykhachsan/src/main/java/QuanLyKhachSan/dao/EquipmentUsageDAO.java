/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentUsageDAO {
    private Connection connection;

    public EquipmentUsageDAO(Connection connection) {
        this.connection = connection;
    }

    public void addEquipmentUsage(EquipmentUsage usage) throws SQLException {
        String sql = "INSERT INTO equipment_usage (room_id, equipment_id, quantity) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, usage.getRoom().getId());
        stmt.setString(2, usage.getEquipment().getEquipmentID());
        stmt.setInt(3, usage.getQuantity());
        stmt.executeUpdate();
    }

    public List<EquipmentUsage> getEquipmentUsageByRoomId(int roomId) throws SQLException {
        List<EquipmentUsage> usages = new ArrayList<>();
        String sql = "SELECT * FROM equipment_usage WHERE room_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, roomId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            EquipmentUsage usage = new EquipmentUsage(
                new Room(rs.getInt("room_id"), "", 0),
                new Equipment(rs.getString("equipment_id"), "", ""),
                rs.getInt("quantity")
            );
            usages.add(usage);
        }
        return usages;
    }
}

