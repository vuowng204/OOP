package com.mycompany.quanlykhachsan.model;

import java.util.Date;

public class Employee extends User {
    private String emID;
    private String position;
    private Date emDateOfBirth;
    private double salary;

    public Employee(String userID, String username, String password, String fullName, String role,
                    String emID, String position, Date emDateOfBirth, double salary) {
        super(userID, username, password, fullName, role);
        this.emID = emID;
        this.position = position;
        this.emDateOfBirth = emDateOfBirth;
        this.salary = salary;
    }

    // Phương thức quản lý phòng
    public void manageRoom(Room room) {
        // Logic quản lý phòng
    }

    // Phương thức quản lý dịch vụ
    public void manageService(Service service) {
        // Logic quản lý dịch vụ
    }

    // Getter và Setter
    public String getEmID() { return emID; }
    public String getPosition() { return position; }
    public Date getEmDateOfBirth() { return emDateOfBirth; }
    public double getSalary() { return salary; }
}
