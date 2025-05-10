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
import view.tkhd;
import javax.swing.JFrame;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JTable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.table.TableModel;

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
    private ServiceNRoom serviceNRoom;

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
        this.serviceNRoom = serviceNRoom;
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
            serviceNRoom.getJDialog1().setVisible(true);
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

    public void calculateTralai(double tienKhachTra) {
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

    public void fillInvoiceInfo(Booking booking, Room room, Customer customer) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            // 1. ID Hóa đơn
            view.getJTextField2().setText(String.format("HD%04d", booking.getId()));

            // 2. Ngày đặt phòng (tức ngày check-in)
            if (booking.getCheckInDate() != null) {
                view.getJTextField10().setText(sdf.format(booking.getCheckInDate()));
            }

            // 3. Mã phòng
            view.getJTextField4().setText(String.valueOf(room.getId()));
            // 4. Tên khách hàng
            view.getJTextField8().setText(customer.getTenKhachHang());

            // 5. Ngày trả phòng (check-out)
            if (booking.getCheckOutDate() != null) {
                view.getJTextField7().setText(sdf.format(booking.getCheckOutDate()));
            }

            // 6. Thời gian đến (check-in)
            if (booking.getCheckInDate() != null) {
                view.getJTextField10().setText(sdf.format(booking.getCheckInDate()));
            }

         

            // 8. Ngày lưu trú
            view.getJTextField12().setText(view.getJTextPane11().getText());

            // 9. Trạng thái hóa đơn
            view.getJTextField13().setText(booking.getStatus());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi điền thông tin hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị danh sách dịch vụ đã sử dụng lên bảng jTable4 trong jDialog1
    public void loadServiceInvoiceTable(List<ServiceUsage> usages) {
        DefaultTableModel model = (DefaultTableModel) view.getJTable4().getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        int stt = 1;
        double tongDichVu = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (ServiceUsage usage : usages) {
            String ngay = sdf.format(new java.util.Date());
            String phong = view.getTenPhong();
            String tenDV = usage.getService().getServiceName();
            int soLuong = usage.getQuantity();
            double donGia = usage.getService().getServicePrice();
            double thanhTien = donGia * soLuong;
            tongDichVu += thanhTien;

            model.addRow(new Object[]{
                stt++,
                ngay,
                phong,
                tenDV,
                soLuong,
                String.format("%,.0f", donGia),
                String.format("%,.0f", thanhTien)
            });
        }

        // Lấy giá phòng (chuỗi dạng "450.000 VND") → cần xử lý lại thành số
        String giaPhongStr = view.getGiaphongjLabel2().getText(); // Ví dụ: "|  Giá Phòng 450.000d"
        double giaPhong = 0;
        try {
            giaPhongStr = giaPhongStr.replaceAll("[^0-9]", ""); // Xóa ký tự không phải số
            giaPhong = Double.parseDouble(giaPhongStr);
        } catch (NumberFormatException e) {
            // Nếu không lấy được giá thì báo lỗi, vẫn tiếp tục
            System.err.println("Lỗi chuyển đổi giá phòng: " + e.getMessage());
        }

        double tongTien = tongDichVu + giaPhong;

        // Thêm dòng Tổng cộng vào bảng
        model.addRow(new Object[]{
            "", "", "", "Tổng cộng", "", "", String.format("%,.0f", tongTien)
        });

        // Ghi tổng tiền vào TextField trong hóa đơn nếu có
        if (view.getJDialog1() != null) {
            try {
                view.getTongTienTextField().setText(String.format("%,.0f", tongTien));
            } catch (Exception e) {
                System.err.println("Không thể setText cho tổng tiền: " + e.getMessage());
            }
        }
    }

    public static BufferedImage captureComponent(Component component) {
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = image.createGraphics();
        component.paint(g2d);
        g2d.dispose();
        return image;
    }

    public void exportComponentToPDF(Component component, String filePath) {
        try {
            // Render trước khi chụp ảnh
            if (component instanceof JDialog) {
                JDialog dialog = (JDialog) component;
                dialog.pack(); // tính lại layout
                dialog.setVisible(true); // đảm bảo render
                dialog.repaint();
            }

            int width = component.getWidth();
            int height = component.getHeight();

            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width (" + width + ") and height (" + height + ") cannot be <= 0");
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            component.printAll(g2d);
            g2d.dispose();

            Document document = new Document(new Rectangle(width, height));
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            Image pdfImage = Image.getInstance(image, null);
            document.add(pdfImage);
            document.close();

            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setTotalAmountToDialog() {
        try {
            double tongTien = 0;
            JTable table = view.getJTable4();
            TableModel model = table.getModel();
            for (int row = 0; row < model.getRowCount(); row++) {
                String giaStr = model.getValueAt(row, 6).toString().replace(",", "");
                tongTien += Double.parseDouble(giaStr);
            }

            // Cộng thêm tiền phòng
            String giaPhongText = view.getJTextPane2().getText().replace(",", "").replace("VND", "").trim();
            if (!giaPhongText.isEmpty()) {
                tongTien += Double.parseDouble(giaPhongText);
            }

            view.getTongtienjTextPane4().setText(String.format("%,.0f", tongTien));

        } catch (Exception e) {
            System.err.println("Không thể tính tổng tiền: " + e.getMessage());
        }
    }
}
