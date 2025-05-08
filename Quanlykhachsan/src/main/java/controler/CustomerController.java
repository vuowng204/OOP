package controler;

import QuanLyKhachSan.dao.BookingDAO;
import QuanLyKhachSan.dao.CustomerDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Customer;

import java.sql.*;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CustomerController {

    private final Connection connection = JDBCConnection.getConnection();
    private final CustomerDAO customerDAO = new CustomerDAO(connection);
    private final BookingDAO bookingDAO = new BookingDAO(connection);

    public void loadData(DefaultTableModel model) {
        model.setRowCount(0);
        HashSet<String> cccdDaThem = new HashSet<>();

        try {
            // 1. Khách từ bookings
            String sql1 = """
            SELECT 
                c.cccd, c.ten_khach_hang, c.so_dien_thoai, c.gioi_tinh,
                b.room_id, b.check_in_date, b.check_out_date
            FROM customers c
            JOIN bookings b ON c.cccd = b.customer_cccd
        """;
            ResultSet rs1 = connection.createStatement().executeQuery(sql1);
            while (rs1.next()) {
                String cccd = rs1.getString("cccd");
                cccdDaThem.add(cccd);
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

            // 2. Khách chỉ có trong bảng customers
            String sql2 = "SELECT * FROM customers";
            ResultSet rs2 = connection.createStatement().executeQuery(sql2);
            while (rs2.next()) {
                String cccd = rs2.getString("cccd");
                if (!cccdDaThem.contains(cccd)) {
                    model.addRow(new Object[]{
                        cccd,
                        rs2.getString("ten_khach_hang"),
                        rs2.getString("so_dien_thoai"),
                        rs2.getString("gioi_tinh"),
                        null, null, null
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByCCCD(String cccd) {
        try {
            // Xóa ở bookings trước
            if (bookingDAO.existsInBooking(cccd)) {
                bookingDAO.deleteFromBooking(cccd);
            }

            // Xóa ở bảng customers
            if (customerDAO.customerExists(cccd)) {
                customerDAO.deleteFromCustomer(cccd);
            }

            JOptionPane.showMessageDialog(null, "Xóa khách hàng thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }

    public void updateCustomer(String cccd, String name, String phone, String gender) {
        try {
            if (customerDAO.customerExists(cccd)) {
                Customer customer = new Customer();
                customer.setCccd(cccd);
                customer.setTenKhachHang(name);
                customer.setSoDienThoai(phone);
                customer.setGioiTinh(gender);
                customerDAO.updateCustomer(customer);
                JOptionPane.showMessageDialog(null, "Cập nhật khách hàng thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Khách hàng không tồn tại để cập nhật.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật khách hàng: " + e.getMessage());
        }
    }

    public void search(DefaultTableModel model, String keyword, String type) {
        model.setRowCount(0);
        String sql = "";

        if (type.equals("Theo tên")) {
            sql = """
            SELECT c.cccd, c.ten_khach_hang, c.so_dien_thoai, c.gioi_tinh, b.room_id, b.check_in_date, b.check_out_date
            FROM customers c
            LEFT JOIN bookings b ON c.cccd = b.customer_cccd
            WHERE c.ten_khach_hang LIKE ?
        """;
        } else if (type.equals("Theo CCCD")) {
            sql = """
            SELECT c.cccd, c.ten_khach_hang, c.so_dien_thoai, c.gioi_tinh, b.room_id, b.check_in_date, b.check_out_date
            FROM customers c
            LEFT JOIN bookings b ON c.cccd = b.customer_cccd
            WHERE c.cccd LIKE ?
        """;
        } else {
            sql = """
            SELECT c.cccd, c.ten_khach_hang, c.so_dien_thoai, c.gioi_tinh, b.room_id, b.check_in_date, b.check_out_date
            FROM customers c
            LEFT JOIN bookings b ON c.cccd = b.customer_cccd
        """;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            if (!type.equals("Theo Ngày")) {
                stmt.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("cccd"),
                    rs.getString("ten_khach_hang"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("gioi_tinh"),
                    rs.getInt("room_id"),
                    rs.getTimestamp("check_in_date"),
                    rs.getTimestamp("check_out_date")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }
}
