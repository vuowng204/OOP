package com.mycompany.quanlykhachsan.model;

public class Room {
    private int id;
    private String tenPhong;
    private RoomType loaiphong;
    private double giaPhong;
    private String tang;
    private String trangThai;

    public Room() {
    }

    public Room(int id, String tenPhong, double giaPhong) {
        this.id = id;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
    }

    public Room(int id, String tenPhong, RoomType loaiphong, double giaPhong, String tang, String trangThai) {
   }

    public RoomType getLoaiphong() {
        return loaiphong;
    }

    public void setLoaiphong(RoomType loaiphong) {
        this.loaiphong = loaiphong;
    }

    public String getTang() {
        return tang;
    }

    public void setTang(String tang) {
        this.tang = tang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }
}    