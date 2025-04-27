/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm khách hàng mới
    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (cccd, ten_khach_hang, so_dien_thoai, gioi_tinh) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, customer.getCccd());
        stmt.setString(2, customer.getTenKhachHang());
        stmt.setString(3, customer.getSoDienThoai());
        stmt.setString(4, customer.getGioiTinh());
        stmt.executeUpdate();
    }

    // Kiểm tra xem khách hàng đã tồn tại chưa
    public boolean customerExists(String cccd) throws SQLException {
        String query = "SELECT COUNT(*) FROM customers WHERE cccd = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, cccd);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}