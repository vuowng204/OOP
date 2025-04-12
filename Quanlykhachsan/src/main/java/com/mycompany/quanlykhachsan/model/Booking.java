package com.mycompany.quanlykhachsan.model;

import java.util.Date;
import java.util.List;

public class Booking {
    private String bookingID;
    private User customer;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private List<Service> services;

    public Booking(String bookingID, User customer, Room room, Date checkInDate, Date checkOutDate, List<Service> services) {
        this.bookingID = bookingID;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.services = services;
    }

    // Phương thức tính tổng chi phí
    public double calculateTotal() {
        double total = room.getPrice();
        for (Service service : services) {
            total += service.getServicePrice();
        }
        return total;
    }

    // Getter và Setter
    public String getBookingID() { return bookingID; }
    public User getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public List<Service> getServices() { return services; }
}
