package controler;

import QuanLyKhachSan.dao.JDBCConnection;
import java.sql.*;
import java.util.HashSet;
import javax.swing.table.DefaultTableModel;

public class CustomerController {
    private final Connection connection = JDBCConnection.getConnection();

    // Load dữ liệu từ bảng customers và bookings (gộp, tránh trùng)
    public void loadData(DefaultTableModel model) {
        model.setRowCount(0);
        HashSet<String> loadedCCCDs = new HashSet<>();

        try {
            // 1. Load khách hàng có booking
            String sql1 = """
                SELECT 
                    c.cccd,
                    c.ten_khach_hang,
                    c.so_dien_thoai,
                    c.gioi_tinh,
                    b.room_id,
                    b.check_in_date,
                    b.check_out_date
                FROM customers c
                JOIN bookings b ON c.cccd = b.customer_cccd
            """;
            Statement stmt1 = connection.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql1);
            while (rs1.next()) {
                String cccd = rs1.getString("cccd");
                loadedCCCDs.add(cccd); // đánh dấu đã hiển thị

                model.addRow(new Object[]{
                    cccd,
                    rs1.getString("ten_khach_hang"),
                    rs1.getString("so_dien_thoai"),
                    rs1.getString("gioi_tinh"),
                    rs1.getInt("room_id"),
                    rs1.getTimestamp("check_in_date"),
                    rs1.getTimestamp("check_out_date")
                });
            }

            // 2. Load khách chưa từng booking (chỉ có trong bảng customers)
            String sql2 = "SELECT * FROM customers";
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);
            while (rs2.next()) {
                String cccd = rs2.getString("cccd");
                if (!loadedCCCDs.contains(cccd)) {
                    model.addRow(new Object[]{
                        cccd,
                        rs2.getString("ten_khach_hang"),
                        rs2.getString("so_dien_thoai"),
                        rs2.getString("gioi_tinh"),
                        null, null, null // chưa booking
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm khách hàng theo tên hoặc CCCD
    public void search(DefaultTableModel model, String keyword, String type) {
        model.setRowCount(0);
        String sql;
        if ("Theo tên".equals(type)) {
            sql = "SELECT * FROM customers WHERE ten_khach_hang LIKE ?";
        } else if ("Theo CCCD".equals(type)) {
            sql = "SELECT * FROM customers WHERE cccd LIKE ?";
        } else {
            sql = "SELECT * FROM customers"; // fallback
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("cccd"),
                    rs.getString("ten_khach_hang"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("gioi_tinh"),
                    null, null, null
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa khách hàng theo CCCD
    public void deleteByCCCD(String cccd) {
        try {
            String sql = "DELETE FROM customers WHERE cccd = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cccd);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(String cccd, String name, String phone, String gender) {
        try {
            String sql = "UPDATE customers SET ten_khach_hang = ?, so_dien_thoai = ?, gioi_tinh = ? WHERE cccd = ?";
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
