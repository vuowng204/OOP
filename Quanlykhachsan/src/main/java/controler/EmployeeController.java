package controler;

import QuanLyKhachSan.dao.EmployeeDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Employee;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class EmployeeController {

    private final Connection connection = JDBCConnection.getConnection();
    private final EmployeeDAO dao = new EmployeeDAO(connection);

    // Load danh sách nhân viên vào bảng
    public void loadData(DefaultTableModel model) {
        try {
            List<Employee> list = dao.getAllEmployees();
            model.setRowCount(0); // Xóa dữ liệu cũ
            for (Employee e : list) {
                model.addRow(new Object[]{
                    e.getEmID(),
                    e.getPosition(),
                    e.getEmDateOfBirth(),
                    e.getSalary()
                });
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải danh sách nhân viên: " + e.getMessage());
        }
    }

    // Xóa nhân viên theo mã
    public void deleteById(String emID) {
        try {
            dao.deleteEmployee(emID);
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    // Cập nhật thông tin nhân viên
    public void update(Employee emp) {
        try {
            dao.updateEmployee(emp);
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thông tin nhân viên: " + e.getMessage());
        }
    }

    public void add(Employee emp) {
        try {
            dao.addEmployee(emp);
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }

    public void search(String keyword, String type, DefaultTableModel model) {
        try {
            List<Employee> list = dao.searchEmployees(keyword, type);
            model.setRowCount(0);
            for (Employee e : list) {
                model.addRow(new Object[]{
                    e.getEmID(),
                    e.getPosition(),
                    e.getEmDateOfBirth(),
                    e.getSalary()
                });
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
        }
    }
}
