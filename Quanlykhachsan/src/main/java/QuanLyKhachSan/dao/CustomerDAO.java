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
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getCccd());
            stmt.setString(2, customer.getTenKhachHang());
            stmt.setString(3, customer.getSoDienThoai());
            stmt.setString(4, customer.getGioiTinh());
            stmt.executeUpdate();
        }
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE customers SET ten_khach_hang = ?, so_dien_thoai = ?, gioi_tinh = ? WHERE cccd = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getTenKhachHang());
            stmt.setString(2, customer.getSoDienThoai());
            stmt.setString(3, customer.getGioiTinh());
            stmt.setString(4, customer.getCccd());
            stmt.executeUpdate();
        }
    }

    // Xóa khách hàng theo CCCD
    public void deleteFromCustomer(String cccd) throws SQLException {
        String query = "DELETE FROM customers WHERE cccd = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cccd);
            stmt.executeUpdate();
        }
    }

    // Kiểm tra khách hàng có tồn tại trong bảng customer không
    public boolean customerExists(String cccd) throws SQLException {
        String query = "SELECT COUNT(*) FROM customers WHERE cccd = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public ResultSet search(String keyword, String type) throws SQLException {
        String query = "";
        switch (type) {
            case "Theo tên":
                query = "SELECT * FROM bookings WHERE ten_khach_hang LIKE ?";
                break;
            case "Theo CCCD":
                query = "SELECT * FROM bookings WHERE cccd LIKE ?";
                break;
            case "Theo Ngày Check-in":
                query = "SELECT * FROM bookings WHERE DATE(check_in) = ?";
                break;
            case "Theo Ngày Check-out":
                query = "SELECT * FROM bookings WHERE DATE(check_out) = ?";
                break;
            default:
                throw new IllegalArgumentException("Loại tìm kiếm không hợp lệ: " + type);
        }

        PreparedStatement stmt = connection.prepareStatement(query);
        if (type.equals("Theo tên") || type.equals("Theo CCCD")) {
            stmt.setString(1, "%" + keyword + "%");
        } else {
            stmt.setString(1, keyword); // ngày đã chuẩn hóa định dạng yyyy-MM-dd
        }

        return stmt.executeQuery();
    }
}