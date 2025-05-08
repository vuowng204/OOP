package controler;

import QuanLyKhachSan.dao.BookingDAO;
import QuanLyKhachSan.dao.RoomDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Booking;
import com.mycompany.quanlykhachsan.model.Room;
import view.tkhd;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class InvoiceController {
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;

    public InvoiceController() {

        Connection conn = JDBCConnection.getConnection();
        bookingDAO = new BookingDAO(conn);
        roomDAO = new RoomDAO(conn);
        
    }
    public void openInvoiceView(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);

            if (booking == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn với ID = " + bookingId);
                return;
            }

            Room room = roomDAO.getRoomByIdWithTypeInfo(booking.getRoomId());

            if (room == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin phòng cho hóa đơn!");
                return;
            }

            tkhd invoiceView = new tkhd(booking, room);
            invoiceView.setLocationRelativeTo(null);
            invoiceView.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối hoặc truy vấn CSDL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}