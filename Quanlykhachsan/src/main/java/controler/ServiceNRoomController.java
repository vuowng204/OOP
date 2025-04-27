/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

/**
 *
 * @author Admin
 */
import QuanLyKhachSan.dao.BookingDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import QuanLyKhachSan.dao.RoomDAO;
import QuanLyKhachSan.dao.ServiceDAO;
import QuanLyKhachSan.dao.ServiceUsageDAO;
import com.mycompany.quanlykhachsan.model.Booking;
import com.mycompany.quanlykhachsan.model.Customer;
import com.mycompany.quanlykhachsan.model.Room;
import com.mycompany.quanlykhachsan.model.Service;
import com.mycompany.quanlykhachsan.model.ServiceUsage;
import java.awt.Color;
import view.ServiceNRoom;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ServiceNRoomController {
    private ServiceNRoom view;
    private ServiceDAO serviceDAO;
    private ServiceUsageDAO serviceUsageDAO;
    private BookingDAO bookingDAO;
    private int bookingId;
    private boolean isCheckedIn;
    private double roomPrice;
    private double servicePrice;
    private double totalPrice;
    private Timestamp checkOutDate;
    private RoomDAO roomDAO;

public ServiceNRoomController(ServiceNRoom view, int bookingId, boolean isCheckedIn) {
    this.view = view;
    this.bookingId = bookingId;
    this.isCheckedIn = isCheckedIn;
    this.bookingDAO = new BookingDAO(JDBCConnection.getConnection());
    this.serviceDAO = new ServiceDAO(JDBCConnection.getConnection());
    this.serviceUsageDAO = new ServiceUsageDAO(JDBCConnection.getConnection());
    this.roomDAO = new RoomDAO(JDBCConnection.getConnection());
    this.roomPrice = 0.0;
    this.servicePrice = 0.0;
    this.totalPrice = 0.0;
    this.checkOutDate = null;
}
    

    // Tìm kiếm dịch vụ
    public void searchServices(String keyword) {
        try {
            List<Service> services = keyword.isEmpty() ? serviceDAO.getAllServices() : serviceDAO.searchServices(keyword);
            DefaultTableModel model = (DefaultTableModel) view.getJTable2().getModel();
            model.setRowCount(0);
            for (Service service : services) {
                model.addRow(new Object[]{
                    service.getServiceID(),
                    service.getServiceName(),
                    String.format("%,.0f VND", service.getServicePrice()),
                    service.getQuantityInStock()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tìm kiếm dịch vụ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Thêm dịch vụ
public void addService(int selectedRow, int quantity) {
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(view, "Vui lòng chọn một dịch vụ từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!isCheckedIn || bookingId == -1) {
        JOptionPane.showMessageDialog(view, "Phòng chưa được check-in! Vui lòng check-in trước khi thêm dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (quantity <= 0) {
        JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        String serviceId = (String) view.getJTable2().getValueAt(selectedRow, 0);
        
        // Kiểm tra số lượng tồn kho
        int quantityInStock = serviceDAO.getQuantityInStock(serviceId);
        if (quantity > quantityInStock) {
            JOptionPane.showMessageDialog(view, "Số lượng tồn kho không đủ! Chỉ còn " + quantityInStock + " đơn vị.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Service> services = serviceDAO.getAllServices();
        Service selectedService = services.stream()
            .filter(s -> s.getServiceID().equals(serviceId))
            .findFirst()
            .orElse(null);

        if (selectedService == null) {
            JOptionPane.showMessageDialog(view, "Dịch vụ không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = bookingDAO.getLatestBookingByRoomId(view.getRoomId());
        if (booking == null || !booking.getStatus().equals("Đang sử dụng")) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy hóa đơn đang sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật bookingId
        this.bookingId = booking.getId();
        Customer customer = booking.getCustomer();
        serviceUsageDAO.addServiceUsage(bookingId, selectedService, quantity, customer);

        // Cập nhật số lượng tồn kho
        serviceDAO.updateQuantityInStock(serviceId, quantity);

        // Cập nhật bảng dịch vụ đã sử dụng
        loadServiceUsages();

        // Cập nhật lại bảng danh sách dịch vụ
        searchServices(""); // Làm mới danh sách dịch vụ

        JOptionPane.showMessageDialog(view, "Thêm dịch vụ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi thêm dịch vụ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    // Tải danh sách dịch vụ đã sử dụng
public void loadServiceUsages() {
    try {
        List<ServiceUsage> serviceUsages = serviceUsageDAO.getServiceUsagesByBookingId(bookingId);
        DefaultTableModel model = (DefaultTableModel) view.getJTable3().getModel();
        model.setRowCount(0);
        for (ServiceUsage usage : serviceUsages) {
            model.addRow(new Object[]{
                String.format("HD%04d", bookingId),
                usage.getCustomer().getTenKhachHang(),
                usage.getService().getServiceName(),
                usage.getQuantity() // Hiển thị số lượng thay vì tổng giá
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi tải danh sách dịch vụ đã sử dụng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    // Làm mới danh sách dịch vụ đã sử dụng
public void refreshServiceUsages() {
    try {
        // Làm mới bảng jTable3 (danh sách dịch vụ đã sử dụng)
        Booking latestBooking = bookingDAO.getLatestBookingByRoomId(view.getRoomId());
        if (latestBooking == null || !latestBooking.getStatus().equals("Đang sử dụng")) {
            DefaultTableModel model = (DefaultTableModel) view.getJTable3().getModel();
            model.setRowCount(0);
            JOptionPane.showMessageDialog(view, "Phòng hiện tại không có hóa đơn đang sử dụng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            this.bookingId = latestBooking.getId();
            this.isCheckedIn = true;
            loadServiceUsages();
        }

        // Làm mới bảng jTable2 (danh sách dịch vụ có sẵn) để hiển thị đầy đủ
        searchServices(""); // Gọi với từ khóa rỗng để hiển thị tất cả dịch vụ
        JOptionPane.showMessageDialog(view, "Đã làm mới danh sách dịch vụ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi làm mới danh sách dịch vụ đã sử dụng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
   public void calculateBill() {
    try {
        // Lấy thông tin đặt phòng
        Booking booking = bookingDAO.getLatestBookingByRoomId(view.getRoomId());
        if (booking == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tính thời gian check-out
        checkOutDate = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        view.getJTextPane9().setText(sdf.format(checkOutDate));

        // Tính tổng thời gian
        long durationMillis = checkOutDate.getTime() - booking.getCheckInDate().getTime();
        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);
        view.getJTextPane11().setText(hours + " giờ " + minutes + " phút");

        // Tính tiền phòng
        Room room = roomDAO.getRoomById(view.getRoomId());
        double hourlyRate = room.getGiaPhong();
        roomPrice = hours * hourlyRate + (minutes > 0 ? hourlyRate : 0);
        view.getJTextPane2().setText(String.format("%,.0f VND", roomPrice)); // otienphongjTextPane2

        // Tính tiền dịch vụ
        List<ServiceUsage> serviceUsages = serviceUsageDAO.getServiceUsagesByBookingId(bookingId);
        servicePrice = 0.0;
        for (ServiceUsage usage : serviceUsages) {
            Service service = serviceDAO.getServiceById(usage.getService().getServiceID());
            servicePrice += service.getServicePrice() * usage.getQuantity();
        }
        view.getJTextPane3().setText(String.format("%,.0f VND", servicePrice)); // otiendichvujTextPane3

        // Tính tổng tiền
        totalPrice = roomPrice + servicePrice;
        view.getJTextPane4().setText(String.format("%,.0f VND", totalPrice)); // tongtienjTextPane4

       
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi tính tiền: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
   public void checkOut() {
    try {
        // Cập nhật thời gian check-out và các khoản tiền vào bảng bookings
        bookingDAO.updateCheckOutDetails(bookingId, checkOutDate, roomPrice, servicePrice, totalPrice);

        // Cập nhật trạng thái hóa đơn
        view.getTrangThaiHD().setText("Đã thanh toán");
        view.getTrangThaiHD().setBackground(Color.GRAY);

        // Cập nhật trạng thái phòng
        roomDAO.updateRoomStatus(view.getRoomId(), "Trống");
        view.getHome().setPhongDaCheckOut(view.getRoomId());

        // Cập nhật trạng thái giao diện
        isCheckedIn = false;
        bookingId = -1;
        view.getCheckinjButton5().setEnabled(true);
        view.getJButton18().setEnabled(false);
        view.getTinhTienjButton7().setEnabled(false);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi check-out: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    public void calculateTralai(double tienKhachTra){
            try {
        // Lấy thông tin đặt phòng
        Booking booking = bookingDAO.getLatestBookingByRoomId(view.getRoomId());
        if (booking == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tính thời gian check-out
        checkOutDate = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        

        // Tính tổng thời gian
        long durationMillis = checkOutDate.getTime() - booking.getCheckInDate().getTime();
        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);
        

        // Tính tiền phòng
        Room room = roomDAO.getRoomById(view.getRoomId());
        double hourlyRate = room.getGiaPhong();
        roomPrice = hours * hourlyRate + (minutes > 0 ? hourlyRate : 0);
        

        // Tính tiền dịch vụ
        List<ServiceUsage> serviceUsages = serviceUsageDAO.getServiceUsagesByBookingId(bookingId);
        servicePrice = 0.0;
        for (ServiceUsage usage : serviceUsages) {
            Service service = serviceDAO.getServiceById(usage.getService().getServiceID());
            servicePrice += service.getServicePrice() * usage.getQuantity();
        }
        

        // Tính tổng tiền
        totalPrice = roomPrice + servicePrice;
       

        // Tính tiền thối lại
        double tienThoiLai = tienKhachTra - totalPrice;
        if (tienThoiLai < 0) {
            JOptionPane.showMessageDialog(view, "Số tiền khách trả không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        view.getTienThoiLaikhacjTextPane2().setText(String.format("%,.0f VND", tienThoiLai));

        // Vô hiệu hóa nút tính tiền sau khi đã tính
    
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Lỗi khi tính tiền: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
        
    }

}