package controler;

import QuanLyKhachSan.dao.EmployeeDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    private final EmployeeDAO dao = new EmployeeDAO(JDBCConnection.getConnection());

    // Load toàn bộ nhân viên vào bảng
    public void loadData(DefaultTableModel model) {
        try {
            List<Employee> list = dao.getAll();
            model.setRowCount(0);
            for (Employee e : list) {
                model.addRow(new Object[]{
                    e.getEmID(),
                    e.getName(),
                    e.getPhone(),
                    e.getGender(),
                    e.getEmail(),
                    e.getSalary(),
                    e.getRole()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu nhân viên: " + e.getMessage());
        }
    }

    // Thêm nhân viên
    public void add(Employee emp) {
        try {
            dao.add(emp);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }

    // Sửa nhân viên
    public void update(Employee emp) {
        try {
            dao.update(emp);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật nhân viên: " + e.getMessage());
        }
    }

    // Xóa nhân viên theo ID
    public void deleteById(String emID) {
        try {
            dao.delete(emID);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    // Tìm kiếm nhân viên theo tên, ngày hoặc CCCD
    public void searchEmployee(DefaultTableModel model, String keyword, String type) {
        try {
            List<Employee> result = dao.search(keyword, type);
            model.setRowCount(0);
            for (Employee emp : result) {
                model.addRow(new Object[]{
                    emp.getEmID(),
                    emp.getName(),
                    emp.getPhone(),
                    emp.getGender(),
                    emp.getEmail(),
                    emp.getSalary(),
                    emp.getRole()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
