 package com.mycompany.quanlykhachsan.model;

public class Room {
    private int id;
    private String tenPhong;
    private RoomType loaiphong;
    private double giaPhong;
    private String tang;
    public Room() {
    }

    public Room(int id, String tenPhong, double giaPhong) {
        this.id = id;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
    }

    public Room(int id, String tenPhong, RoomType loaiphong, double giaPhong, String tang) {
        this.id = id;
        this.tenPhong = tenPhong;
        this.loaiphong = loaiphong;
        this.giaPhong = giaPhong;
        this.tang = tang;
    }

    public RoomType getLoaiphong() {
        return loaiphong;
    }

    public String getTang() {
        return tang;
    }

    public void setLoaiphong(RoomType loaiphong) {
        this.loaiphong = loaiphong;
    }

    public void setTang(String tang) {
        this.tang = tang;
    }
    

    public int getId() {
        return id;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
}

    
   
    
   

  