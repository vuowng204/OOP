package com.mycompany.quanlykhachsan.model;

public class Room {
    private String roomID;
    private RoomType roomType;
    private boolean isAvailable;
    private double price;

    public Room(String roomID, RoomType roomType, boolean isAvailable, double price) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    // Phương thức kiểm tra phòng trống
    public boolean checkAvailability() {
        return isAvailable;
    }

    // Phương thức đặt phòng
    public void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Phòng " + roomID + " đã được đặt.");
        } else {
            System.out.println("Phòng " + roomID + " hiện không có sẵn.");
        }
    }

    // Getter và Setter
    public String getRoomID() { return roomID; }
    public RoomType getRoomType() { return roomType; }
    public boolean isAvailable() { return isAvailable; }
    public double getPrice() { return price; }
}
