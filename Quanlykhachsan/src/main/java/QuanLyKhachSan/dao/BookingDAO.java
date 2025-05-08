/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Booking;
import com.mycompany.quanlykhachsan.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class BookingDAO {

    private Connection connection;

    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    public int createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (room_id, customer_name, customer_cccd, customer_phone, customer_gender, user_id, check_in_date, status, number_of_people) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getRoomId());
            stmt.setString(2, booking.getCustomer().getTenKhachHang());
            stmt.setString(3, booking.getCustomer().getCccd());
            stmt.setString(4, booking.getCustomer().getSoDienThoai());
            stmt.setString(5, booking.getCustomer().getGioiTinh());
            stmt.setString(6, booking.getUserId());
            stmt.setTimestamp(7, booking.getCheckInDate());
            stmt.setString(8, booking.getStatus());
            stmt.setInt(9, booking.getSoNguoiO());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về ID của bản ghi vừa tạo
            }
            throw new SQLException("Không thể lấy ID của bản ghi vừa tạo.");
        }
    }

    public void updateCheckOutDate(int bookingId, Timestamp checkOutDate) throws SQLException {
        String sql = "UPDATE bookings SET check_out_date = ?, status = 'Đã thanh toán' WHERE id = ? AND status = 'Đang sử dụng'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, checkOutDate);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
        }
    }

    public Booking getLatestBookingByRoomId(int roomId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE room_id = ? AND status IN ('Đang sử dụng', 'Đã thanh toán') ORDER BY check_in_date DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setRoomId(rs.getInt("room_id"));

                Customer customer = new Customer();
                customer.setTenKhachHang(rs.getString("customer_name"));
                customer.setCccd(rs.getString("customer_cccd"));
                customer.setSoDienThoai(rs.getString("customer_phone"));
                customer.setGioiTinh(rs.getString("customer_gender"));
                booking.setCustomer(customer);

                booking.setUserId(rs.getString("user_id"));
                booking.setCheckInDate(rs.getTimestamp("check_in_date"));
                booking.setCheckOutDate(rs.getTimestamp("check_out_date"));
                booking.setStatus(rs.getString("status"));
                booking.setSoNguoiO(rs.getInt("number_of_people"));
                return booking;
            }
        }
        return null;
    }

    public void updateCheckOutDetails(int bookingId, Timestamp checkOutDate, double roomPrice, double servicePrice, double totalPrice) throws SQLException {
        String sql = "UPDATE bookings SET check_out_date = ?, room_price = ?, service_price = ?, total_price = ?, status = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, checkOutDate);
            pstmt.setDouble(2, roomPrice);
            pstmt.setDouble(3, servicePrice);
            pstmt.setDouble(4, totalPrice);
            pstmt.setString(5, "Đã thanh toán"); // Cập nhật trạng thái hóa đơn
            pstmt.setInt(6, bookingId);
            pstmt.executeUpdate();
        }
    }

    public boolean existsInBooking(String cccd) throws SQLException {
        String sql = "SELECT 1 FROM bookings WHERE customer_cccd = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void deleteFromBooking(String cccd) throws SQLException {
        String query = "DELETE FROM bookings WHERE customer_cccd = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, cccd);
        stmt.executeUpdate();
    }
    public Booking getBookingById(int id) {
    String sql = "SELECT * FROM bookings WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Booking booking = new Booking();
            booking.setId(rs.getInt("id"));
            booking.setRoomId(rs.getInt("room_id"));

            Customer customer = new Customer();
            customer.setTenKhachHang(rs.getString("customer_name"));
            customer.setCccd(rs.getString("customer_cccd"));
            customer.setSoDienThoai(rs.getString("customer_phone"));
            customer.setGioiTinh(rs.getString("customer_gender"));
            booking.setCustomer(customer);

            booking.setUserId(rs.getString("user_id"));
            booking.setCheckInDate(rs.getTimestamp("check_in_date"));
            booking.setCheckOutDate(rs.getTimestamp("check_out_date"));
            booking.setStatus(rs.getString("status"));
            booking.setSoNguoiO(rs.getInt("number_of_people"));

            return booking;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
}
