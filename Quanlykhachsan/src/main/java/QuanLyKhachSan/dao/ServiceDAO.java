package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private Connection connection;

    public ServiceDAO(Connection connection) {
        this.connection = connection;
    }

    // Tìm kiếm dịch vụ theo từ khóa
    public List<Service> searchServices(String keyword) throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM services WHERE service_name LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Service service = new Service(
                rs.getString("service_id"),
                rs.getString("service_name"),
                rs.getDouble("service_price"),
                rs.getInt("quantity_in_stock") // Lấy số lượng tồn kho
            );
            services.add(service);
        }
        return services;
    }

    // Lấy tất cả dịch vụ
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM services";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Service service = new Service(
                rs.getString("service_id"),
                rs.getString("service_name"),
                rs.getDouble("service_price"),
                rs.getInt("quantity_in_stock") // Lấy số lượng tồn kho
            );
            services.add(service);
        }
        return services;
    }

    // Cập nhật số lượng tồn kho sau khi sử dụng dịch vụ
    public void updateQuantityInStock(String serviceId, int usedQuantity) throws SQLException {
        String query = "UPDATE services SET quantity_in_stock = quantity_in_stock - ? WHERE service_id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, usedQuantity);
        stmt.setString(2, serviceId);
        stmt.executeUpdate();
    }

    // Lấy số lượng tồn kho của một dịch vụ
    public int getQuantityInStock(String serviceId) throws SQLException {
        String query = "SELECT quantity_in_stock FROM services WHERE service_id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, serviceId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("quantity_in_stock");
        }
        return 0;
    }
    public Service getServiceById(String serviceId) throws SQLException {
    String sql = "SELECT * FROM services WHERE service_id = ?";
    try (Connection conn = JDBCConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, serviceId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Service service = new Service();
            service.setServiceID(rs.getString("service_id"));
            service.setServiceName(rs.getString("service_name"));
            service.setServicePrice(rs.getDouble("service_price"));
            service.setQuantityInStock(rs.getInt("quantity_in_stock"));
            return service;
        }
        return null;
    }
}
}