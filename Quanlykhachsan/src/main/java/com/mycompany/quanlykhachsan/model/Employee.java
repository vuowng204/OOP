package com.mycompany.quanlykhachsan.model;

import java.util.Date;

public class Employee {

    private String emID;
    private String userID;
    private String position;
    private Date emDateOfBirth;
    private double salary;
    private String gioiTinh;
    private String role;

    public Employee() {
    }

    public Employee(String emID, String userID, String position, Date emDateOfBirth, double salary) {
        this.emID = emID;
        this.userID = userID;
        this.position = position;
        this.emDateOfBirth = emDateOfBirth;
        this.salary = salary;
    }

    public String getEmID() {
        return emID;
    }

    public void setEmID(String emID) {
        this.emID = emID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getEmDateOfBirth() {
        return emDateOfBirth;
    }

    public void setEmDateOfBirth(Date emDateOfBirth) {
        this.emDateOfBirth = emDateOfBirth;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
