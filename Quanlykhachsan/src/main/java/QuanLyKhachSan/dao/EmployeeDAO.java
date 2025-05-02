package QuanLyKhachSan.dao;

import com.mycompany.quanlykhachsan.model.Employee;
import java.sql.*;
import java.util.*;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Employee emp = new Employee();
            emp.setEmID(rs.getString("emID"));
            emp.setPosition(rs.getString("position"));
            emp.setEmDateOfBirth(rs.getDate("emDateOfBirth"));
            emp.setSalary(rs.getDouble("salary"));
            list.add(emp);
        }

        return list;
    }

    public void deleteEmployee(String emID) throws SQLException {
        String sql = "DELETE FROM employees WHERE emID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emID);
        stmt.executeUpdate();
    }

    public void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET position=?, emDateOfBirth=?, salary=? WHERE emID=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emp.getPosition());
        stmt.setDate(2, new java.sql.Date(emp.getEmDateOfBirth().getTime()));
        stmt.setDouble(3, emp.getSalary());
        stmt.setString(4, emp.getEmID());
        stmt.executeUpdate();
    }

    public void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (emID, position, emDateOfBirth, salary) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emp.getEmID());
        stmt.setString(2, emp.getPosition());
        stmt.setDate(3, new java.sql.Date(emp.getEmDateOfBirth().getTime()));
        stmt.setDouble(4, emp.getSalary());
        stmt.executeUpdate();
    }
    public List<Employee> searchEmployees(String keyword, String type) throws SQLException {
    List<Employee> list = new ArrayList<>();
    String sql;

    switch (type) {
        case "Theo tên":
            sql = "SELECT * FROM employees WHERE position LIKE ?";
            break;
        case "Theo Ngày":
            sql = "SELECT * FROM employees WHERE emDateOfBirth LIKE ?";
            break;
        case "Theo CCCD":
            sql = "SELECT * FROM employees WHERE emID LIKE ?";
            break;
        default:
            return list;
    }

    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, "%" + keyword + "%");
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        Employee emp = new Employee();
        emp.setEmID(rs.getString("emID"));
        emp.setPosition(rs.getString("position"));
        emp.setEmDateOfBirth(rs.getDate("emDateOfBirth"));
        emp.setSalary(rs.getDouble("salary"));
        list.add(emp);
    }

    return list;
}
}
