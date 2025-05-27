/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Booking;
import com.mycompany.quanlykhachsan.model.Customer;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean updateBookingMaHD(int bookingId, String maHoaDon) {
        // Câu lệnh SQL để cập nhật cột Ma_HD
        String sql = "UPDATE bookings SET Ma_HD = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Thiết lập các tham số cho câu lệnh SQL
            stmt.setString(1, maHoaDon);  // Tham số thứ nhất: Ma_HD
            stmt.setInt(2, bookingId);    // Tham số thứ hai: id của booking

            // Thực thi câu lệnh cập nhật
            int affectedRows = stmt.executeUpdate();

            // Kiểm tra số hàng bị ảnh hưởng để biết cập nhật có thành công không
            if (affectedRows > 0) {
                System.out.println("Cập nhật mã hóa đơn '" + maHoaDon + "' cho booking ID " + bookingId + " thành công.");
                return true;
            } else {
                System.out.println("Không tìm thấy booking ID " + bookingId + " hoặc không có thay đổi nào được thực hiện.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mã hóa đơn cho booking ID " + bookingId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
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

    public List<Booking> getBookingByRoomID2(int roomId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE room_id = ? AND status IN ('Đang sử dụng', 'Đã thanh toán') ORDER BY total_price DESC ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            // Sử dụng vòng lặp while để duyệt qua tất cả các bản ghi
            while (rs.next()) {
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
                booking.setTongTien(rs.getDouble("total_price"));
                booking.setMaHD(rs.getString("Ma_HD"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách booking: " + e.getMessage());

            e.printStackTrace();
        }
        return bookings; // Trả về danh sách bookings
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
        // 1. Lấy danh sách booking_id tương ứng với khách hàng có CCCD
        String getBookingIds = "SELECT id FROM bookings WHERE customer_cccd = ?";
        PreparedStatement getStmt = connection.prepareStatement(getBookingIds);
        getStmt.setString(1, cccd);
        ResultSet rs = getStmt.executeQuery();

        while (rs.next()) {
            int bookingId = rs.getInt("id");

            // 2. Xóa các dòng liên quan trong service_usage trước
            String deleteServiceUsage = "DELETE FROM service_usage WHERE booking_id = ?";
            PreparedStatement deleteServiceStmt = connection.prepareStatement(deleteServiceUsage);
            deleteServiceStmt.setInt(1, bookingId);
            deleteServiceStmt.executeUpdate();
        }

        // 3. Xóa chính booking
        String deleteBooking = "DELETE FROM bookings WHERE customer_cccd = ?";
        PreparedStatement deleteBookingStmt = connection.prepareStatement(deleteBooking);
        deleteBookingStmt.setString(1, cccd);
        deleteBookingStmt.executeUpdate();
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

    public List<Booking> getBooking() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = 'Đã thanh toán'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            // Sử dụng vòng lặp while để duyệt qua tất cả các bản ghi
            while (rs.next()) {
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
                booking.setTongTien(rs.getDouble("total_price"));

                bookings.add(booking); // Thêm booking vào danh sách
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách booking: " + e.getMessage());
            // Log lỗi chi tiết hơn nếu có thể
            e.printStackTrace();
        }
        return bookings; // Trả về danh sách bookings
    }

    public List<Booking> searchBookingsByRoomIdPartial(String searchRoomIdPart) {
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT id, room_id, customer_name, customer_cccd, customer_phone, customer_gender, "
                + "user_id, check_in_date, check_out_date, status, number_of_people, Ma_HD, total_price "
                + "FROM bookings WHERE CAST(room_id AS CHAR) LIKE ? AND status = 'Đã thanh toán'"; // Sử dụng CAST để tìm kiếm gần đúng với số

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Thêm ký tự '%' để tìm kiếm chuỗi con ở bất kỳ đâu
            stmt.setString(1, "%" + searchRoomIdPart + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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
                booking.setTongTien(rs.getDouble("total_price"));
                booking.setMaHD(rs.getString("Ma_HD"));

                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm booking theo Room ID gần đúng: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    public Map<Integer, Double> getMonthlyRevenueByYear(int year) {
        Map<Integer, Double> monthlyRevenue = new HashMap<>();
        // Đảm bảo check_out_date không NULL để hàm YEAR và MONTH hoạt động đúng
        String sql = "SELECT MONTH(check_out_date) AS month, SUM(total_price) AS monthly_revenue "
                + "FROM bookings "
                + "WHERE YEAR(check_out_date) = ? AND status = 'Đã thanh toán' AND check_out_date IS NOT NULL "
                + "GROUP BY MONTH(check_out_date) "
                + "ORDER BY MONTH(check_out_date)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("month");
                double revenue = rs.getDouble("monthly_revenue");
                monthlyRevenue.put(month, revenue);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu hàng tháng cho năm " + year + ": " + e.getMessage());
            e.printStackTrace();
        }
        return monthlyRevenue;
    }

    public Set<Integer> getDistinctYearsInBookings() {
        Set<Integer> years = new TreeSet<>((y1, y2) -> Integer.compare(y2, y1)); // Sắp xếp giảm dần
        String sql = "SELECT DISTINCT YEAR(check_out_date) AS year FROM bookings WHERE check_out_date IS NOT NULL AND status = 'Đã thanh toán' ORDER BY year DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách các năm: " + e.getMessage());
            e.printStackTrace();
        }
        return years;
    }

    public double getTotalMonth(int t) {
        int y = Calendar.getInstance().get(Calendar.YEAR);
        double tt = 0.0;
        String sql = "SELECT SUM(total_price) AS total FROM bookings "
                + "WHERE MONTH(check_out_date) = ? AND YEAR(check_out_date) = ? "
                + "AND status = 'Đã thanh toán' AND total_price IS NOT NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, t);
            stmt.setInt(2, y);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tt = rs.getDouble("total");
            }
        } catch (SQLException ex) {

            System.out.println("Lỗi Khi Lây tổng doanh thu!!!" + t + "năm :" + y);
            ex.printStackTrace();
        }
        return tt;
    }

    public Map<Integer, Integer> getRoomsBookedMultipleTimes(int month) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Map<Integer, Integer> roomBookingCounts = new LinkedHashMap<>();
        String sql = "SELECT room_id, COUNT(id) AS booking_count "
                + "FROM bookings "
                + "WHERE MONTH(check_in_date) = ? AND YEAR(check_in_date) = ? "
                + "GROUP BY room_id "
                + "HAVING COUNT(id) > 1 "
                + "ORDER BY booking_count DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, currentYear);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                int bookingCount = rs.getInt("booking_count");
                roomBookingCounts.put(roomId, bookingCount);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách phòng đặt nhiều lần tháng " + month + " năm " + currentYear + ": " + e.getMessage());
            e.printStackTrace();
        }
        return roomBookingCounts;
    }

    public void updateCustomerInfo(int bookingId, Customer customer, int soNguoiO) throws SQLException {
        String sql = "UPDATE bookings SET customer_name = ?, customer_cccd = ?, customer_phone = ?, customer_gender = ?, number_of_people = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getTenKhachHang());
            stmt.setString(2, customer.getCccd());
            stmt.setString(3, customer.getSoDienThoai());
            stmt.setString(4, customer.getGioiTinh());
            stmt.setInt(5, soNguoiO);
            stmt.setInt(6, bookingId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy booking với ID: " + bookingId);
            }
        }
    }
    public void updateRoomId(int bookingId, int newRoomId) throws SQLException {
    String sql = "UPDATE bookings SET room_id = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, newRoomId);
        stmt.setInt(2, bookingId);
        stmt.executeUpdate();
    }
}
}
