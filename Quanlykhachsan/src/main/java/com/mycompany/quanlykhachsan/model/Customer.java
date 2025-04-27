package com.mycompany.quanlykhachsan.model;

public class Customer {
    private String cccd;
    private String tenKhachHang;
    private String soDienThoai;
    private String gioiTinh;

    public Customer() {}

    public Customer(String cccd, String tenKhachHang, String soDienThoai, String gioiTinh) {
        this.cccd = cccd;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
    }

    // Getters and setters
    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cccd='" + cccd + '\'' +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                '}';
    }
}