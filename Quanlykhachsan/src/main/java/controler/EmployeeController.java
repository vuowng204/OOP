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
                    e.getEmID(), // 0: CCCD
                    e.getName(), // 1: Tên
                    e.getPhone(), // 2: SĐT
                    e.getGender(), // 3: Giới tính
                    e.getEmail(), // 4: Email
                    e.getSalary(), // 5: Lương
                    e.getRole(), // 6: Chức vụ
                    e.getEmDateOfBirth() // 7: Ngày sinh (phải khớp với jTable1)
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu nhân viên: " + e.getMessage());
        }
    }

    // Thêm nhân viên
    public void add(Employee emp, DefaultTableModel model) {
        try {
            dao.add(emp);
            loadData(model); // cập nhật bảng ngay sau khi thêm
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
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

    // Tìm kiếm nhân viên theo tên, ngày hoặc CCCD, hoặc chức vụ
    public void searchEmployee(DefaultTableModel model, String keyword, String type) {
        model.setRowCount(0);
        try {
            List<Employee> list = dao.search(keyword, type);
            for (Employee e : list) {
                model.addRow(new Object[]{
                    e.getEmID(),
                    e.getName(),
                    e.getPhone(),
                    e.getGender(),
                    e.getEmail(),
                    e.getSalary(),
                    e.getRole(),
                    e.getEmDateOfBirth()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

    public boolean exists(String emID) {
        try {
            return dao.exists(emID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
