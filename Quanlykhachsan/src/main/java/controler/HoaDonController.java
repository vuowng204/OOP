/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import QuanLyKhachSan.dao.BookingDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import com.mycompany.quanlykhachsan.model.Booking;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class HoaDonController {

    private final Connection connection = JDBCConnection.getConnection();
    private final BookingDAO bookingDAO = new BookingDAO(connection);

    public List<Booking> getAllBookings() {
        return bookingDAO.getBooking();
    }

    public List<Booking> searchBookingsByRoomIdPartial(String searchRoomIdPart) {
        return bookingDAO.searchBookingsByRoomIdPartial(searchRoomIdPart);
    } 
    public Map<Integer, Double> getMonthlyRevenueForYear(int year) {
       return bookingDAO.getMonthlyRevenueByYear(year);
   }
    public Set<Integer> getAvailableYears() {
       return bookingDAO.getDistinctYearsInBookings();
   }
    public double getTotalMonth(int t){
       return bookingDAO.getTotalMonth(t);
    }
    public Map<Integer, Integer> getRoomsBookedMultipleTimes(int month) {
         return bookingDAO.getRoomsBookedMultipleTimes(month);
     }
}
