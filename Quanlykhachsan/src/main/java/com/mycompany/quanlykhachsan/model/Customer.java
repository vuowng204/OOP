package com.mycompany.quanlykhachsan.model;

import java.util.Date;

public class Customer {
    private String CCCD;
    private String ten;
    private String sdt;
    private String gioitinh;
    private Room sophong; // liên kết đến Room
    private Date ngayCheckin;
    private Date ngayCheckout;

    public Customer() {}

    public Customer(String CCCD, String ten, String sdt, String gioitinh, Room sophong, Date ngayCheckin, Date ngayCheckout) {
        this.CCCD = CCCD;
        this.ten = ten;
        this.sdt = sdt;
        this.gioitinh = gioitinh;
        this.sophong = sophong;
        this.ngayCheckin = ngayCheckin;
        this.ngayCheckout = ngayCheckout;
    }

    // Getter và Setter
    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public Room getSophong() {
        return sophong;
    }

    public void setSophong(Room sophong) {
        this.sophong = sophong;
    }

    public Date getNgayCheckin() {
        return ngayCheckin;
    }

    public void setNgayCheckin(Date ngayCheckin) {
        this.ngayCheckin = ngayCheckin;
    }

    public Date getNgayCheckout() {
        return ngayCheckout;
    }

    public void setNgayCheckout(Date ngayCheckout) {
        this.ngayCheckout = ngayCheckout;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CCCD='" + CCCD + '\'' +
                ", Ten='" + ten + '\'' +
                ", SĐT='" + sdt + '\'' +
                ", Giới tính='" + gioitinh + '\'' +
                ", Số phòng='" + (sophong != null ? sophong.getTenPhong() : "Chưa chọn") + '\'' +
                ", Ngày check-in=" + (ngayCheckin != null ? ngayCheckin.toString() : "null") +
                ", Ngày check-out=" + (ngayCheckout != null ? ngayCheckout.toString() : "null") +
                '}';
    }
}
