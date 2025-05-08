/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.ServiceUsage;
import com.mycompany.quanlykhachsan.model.Customer;
import com.mycompany.quanlykhachsan.model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsageDAO {

    private Connection connection;

    public ServiceUsageDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm một bản ghi sử dụng dịch vụ
    public void addServiceUsage(int bookingId, Service service, int quantity, Customer customer) throws SQLException {
        String query = "INSERT INTO service_usage (booking_id, service_id, quantity, customer_id) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, bookingId);
        stmt.setString(2, service.getServiceID());
        stmt.setInt(3, quantity);
        stmt.setString(4, customer.getCccd()); // Giả sử customer_id là CCCD
        stmt.executeUpdate();
    }

    // Lấy danh sách dịch vụ đã sử dụng theo bookingId
    public List<ServiceUsage> getServiceUsagesByBookingId(int bookingId) throws SQLException {
        List<ServiceUsage> serviceUsages = new ArrayList<>();
        String query = "SELECT su.*, s.service_name, s.service_price, s.quantity_in_stock, c.ten_khach_hang "
                + "FROM service_usage su "
                + "JOIN services s ON su.service_id = s.service_id "
                + "JOIN customers c ON su.customer_id = c.cccd "
                + "WHERE su.booking_id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, bookingId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Service service = new Service(
                    rs.getString("service_id"),
                    rs.getString("service_name"),
                    rs.getDouble("service_price"),
                    rs.getInt("quantity_in_stock") // Thêm tham số quantityInStock
            );
            Customer customer = new Customer();
            customer.setTenKhachHang(rs.getString("ten_khach_hang"));
            customer.setCccd(rs.getString("customer_id"));

            ServiceUsage usage = new ServiceUsage();
            usage.setService(service);
            usage.setCustomer(customer);
            usage.setQuantity(rs.getInt("quantity"));

            serviceUsages.add(usage);
        }
        return serviceUsages;
    }
    public List<ServiceUsage> getServiceUsageByCustomer(String cccd) {
        List<ServiceUsage> list = new ArrayList<>();
        String sql = "SELECT su.quantity, s.service_id, s.service_name, s.service_price "
                + "FROM service_usage su "
                + "JOIN services s ON su.service_id = s.service_id "
                + "WHERE su.customer_cccd = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Service service = new Service(
                        rs.getString("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("service_price")
                );

                ServiceUsage usage = new ServiceUsage();
                usage.setService(service);
                usage.setQuantity(rs.getInt("quantity"));
                list.add(usage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
