package com.mycompany.quanlykhachsan.model;

import java.sql.Date;

public class Employee {
    private String emID;
    private String name;
    private Date emDateOfBirth;
    private String phone;
    private String gender;
    private String email;
    private double salary;
    private String role;

    public Employee() {}

    public Employee(String emID, String name, Date emDateOfBirth, String phone, String gender, String email, double salary, String role) {
        this.emID = emID;
        this.name = name;
        this.emDateOfBirth = emDateOfBirth;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.salary = salary;
        this.role = role;
    }

    public String getEmID() {
        return emID;
    }

    public void setEmID(String emID) {
        this.emID = emID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEmDateOfBirth() {
        return emDateOfBirth;
    }

    public void setEmDateOfBirth(Date emDateOfBirth) {
        this.emDateOfBirth = emDateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}