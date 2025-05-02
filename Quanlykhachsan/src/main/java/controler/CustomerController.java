/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import QuanLyKhachSan.dao.JDBCConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CustomerController {
    private final Connection connection = JDBCConnection.getConnection();

    public void loadData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM bookings";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("customer_cccd"),
                    rs.getString("customer_name"),
                    rs.getString("customer_phone"),
                    rs.getString("customer_gender"),
                    rs.getInt("room_id"),
                    rs.getTimestamp("check_in_date"),
                    rs.getTimestamp("check_out_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search(DefaultTableModel model, String keyword, String type) {
        model.setRowCount(0);
        try {
            String sql = switch (type) {
                case "Theo tên" -> "SELECT * FROM bookings WHERE customer_name LIKE ?";
                case "Theo CCCD" -> "SELECT * FROM bookings WHERE customer_cccd LIKE ?";
                default -> "SELECT * FROM bookings WHERE DATE(check_in_date) = ?";
            };

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("customer_cccd"),
                    rs.getString("customer_name"),
                    rs.getString("customer_phone"),
                    rs.getString("customer_gender"),
                    rs.getInt("room_id"),
                    rs.getTimestamp("check_in_date"),
                    rs.getTimestamp("check_out_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByCCCD(String cccd) {
        try {
            String sql = "DELETE FROM bookings WHERE customer_cccd = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cccd);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomer(String cccd, String name, String phone, String gender) {
        try {
            String sql = "UPDATE bookings SET customer_name = ?, customer_phone = ?, customer_gender = ? WHERE customer_cccd = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, gender);
            stmt.setString(4, cccd);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
