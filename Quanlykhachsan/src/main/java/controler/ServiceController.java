package controler;

import QuanLyKhachSan.dao.ServiceDAO;
import com.mycompany.quanlykhachsan.model.Service;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ServiceController {
    private ServiceDAO serviceDAO;

    public ServiceController(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    // Tải tất cả dịch vụ
    public void loadAllServices(DefaultTableModel model) {
        try {
            List<Service> services = serviceDAO.getAllServices();
            model.setRowCount(0);
            for (Service s : services) {
                model.addRow(new Object[]{
                    s.getServiceID(),
                    s.getServiceName(),
                    s.getServicePrice(),
                    s.getQuantityInStock()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
        }
    }

    // Tìm kiếm dịch vụ
    public void searchServices(String keyword, DefaultTableModel model) {
        try {
            List<Service> services = serviceDAO.searchServices(keyword);
            model.setRowCount(0);
            for (Service s : services) {
                model.addRow(new Object[]{
                    s.getServiceID(),
                    s.getServiceName(),
                    s.getServicePrice(),
                    s.getQuantityInStock()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm dịch vụ: " + e.getMessage());
        }
    }

    // Lấy thông tin dịch vụ theo ID
    public Service getServiceById(String id) {
        try {
            return serviceDAO.getServiceById(id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy dịch vụ: " + e.getMessage());
            return null;
        }
    }

    // Cập nhật tồn kho sau khi dùng dịch vụ
    public void updateQuantity(String id, int usedQty) {
        try {
            serviceDAO.updateQuantityInStock(id, usedQty);
            JOptionPane.showMessageDialog(null, "Đã cập nhật tồn kho!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật tồn kho: " + e.getMessage());
        }
    }

    // Thêm dịch vụ
    public void addService(Service service, DefaultTableModel model) {
        try {
            serviceDAO.addService(service);
            JOptionPane.showMessageDialog(null, "Thêm dịch vụ thành công!");
            loadAllServices(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm dịch vụ: " + e.getMessage());
        }
    }

    // Cập nhật dịch vụ
    public void updateService(Service service, DefaultTableModel model) {
        try {
            serviceDAO.updateService(service);
            JOptionPane.showMessageDialog(null, "Cập nhật dịch vụ thành công!");
            loadAllServices(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật dịch vụ: " + e.getMessage());
        }
    }

    // Xóa dịch vụ
    public void deleteService(String id, DefaultTableModel model) {
        try {
            serviceDAO.deleteService(id);
            JOptionPane.showMessageDialog(null, "Xóa dịch vụ thành công!");
            loadAllServices(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa dịch vụ: " + e.getMessage());
        }
    }
}
