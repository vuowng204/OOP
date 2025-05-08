package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm nhân viên
    public void add(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (emID, name, emDateOfBirth, phone, gender, email, salary, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emp.getEmID());
        stmt.setString(2, emp.getName());
        stmt.setDate(3, emp.getEmDateOfBirth());
        stmt.setString(4, emp.getPhone());
        stmt.setString(5, emp.getGender());
        stmt.setString(6, emp.getEmail());
        stmt.setDouble(7, emp.getSalary());
        stmt.setString(8, emp.getRole());
        stmt.executeUpdate();
    }

    // Cập nhật nhân viên
    public void update(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET name = ?, emDateOfBirth = ?, phone = ?, gender = ?, email = ?, salary = ?, role = ? WHERE emID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emp.getName());
        stmt.setDate(2, emp.getEmDateOfBirth());
        stmt.setString(3, emp.getPhone());
        stmt.setString(4, emp.getGender());
        stmt.setString(5, emp.getEmail());
        stmt.setDouble(6, emp.getSalary());
        stmt.setString(7, emp.getRole());
        stmt.setString(8, emp.getEmID());
        stmt.executeUpdate();
    }

    // Xóa nhân viên
    public void delete(String emID) throws SQLException {
        String sql = "DELETE FROM employees WHERE emID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emID);
        stmt.executeUpdate();
    }

    // Lấy tất cả nhân viên
    public List<Employee> getAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Employee emp = new Employee();
            emp.setEmID(rs.getString("emID"));
            emp.setName(rs.getString("name"));
            emp.setEmDateOfBirth(rs.getDate("emDateOfBirth"));
            emp.setPhone(rs.getString("phone"));
            emp.setGender(rs.getString("gender"));
            emp.setEmail(rs.getString("email"));
            emp.setSalary(rs.getDouble("salary"));
            emp.setRole(rs.getString("role"));
            list.add(emp);
        }
        return list;
    }

    // Tìm kiếm theo từ khóa và kiểu
    public List<Employee> search(String keyword, String type) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql;

        if ("Theo tên".equals(type)) {
            sql = "SELECT * FROM employees WHERE name LIKE ?";
        } else if ("Theo CCCD".equals(type)) {
            sql = "SELECT * FROM employees WHERE emID LIKE ?";
        } else if ("Theo quyền".equals(type)) {
            sql = "SELECT * FROM employees WHERE role LIKE ?";
        } else {
            sql = "SELECT * FROM employees"; // fallback nếu sai type
        }

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Employee emp = new Employee();
            emp.setEmID(rs.getString("emID"));
            emp.setName(rs.getString("name"));
            emp.setEmDateOfBirth(rs.getDate("emDateOfBirth"));
            emp.setPhone(rs.getString("phone"));
            emp.setGender(rs.getString("gender"));
            emp.setEmail(rs.getString("email"));
            emp.setSalary(rs.getDouble("salary"));
            emp.setRole(rs.getString("role"));
            list.add(emp);
        }

        return list;
    }
}
