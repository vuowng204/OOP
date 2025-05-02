/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {
    private Connection connection;

    public EquipmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addEquipment(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment (equipmentID, name, description) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, equipment.getEquipmentID());
        stmt.setString(2, equipment.getName());
        stmt.setString(3, equipment.getDescription());
        stmt.executeUpdate();
    }

    public List<Equipment> getAllEquipment() throws SQLException {
        List<Equipment> equipments = new ArrayList<>();
        String sql = "SELECT * FROM equipment";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            equipments.add(new Equipment(
                rs.getString("equipmentID"),
                rs.getString("name"),
                rs.getString("description")
            ));
        }
        return equipments;
    }
}
