/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class JDBCConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/quanlynhahang";
    private static final String USER = "root"; // thay bằng username MySQL của bạn
    private static final String PASSWORD = "123456"; // thay bằng mật khẩu MySQL của bạn

    public static Connection getConnection() {
        try {
            // Tải driver JDBC cho MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kết nối và trả về đối tượng Connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại.");
            e.printStackTrace();
        }
        return null;
    }

    // Test thử kết nối
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối thất bại.");
        }
    }
}
    

