package com.mycompany.quanlykhachsan.model;

public class Service {
    private String serviceID;
    private String serviceName;
    private double servicePrice;

    public Service(String serviceID, String serviceName, double servicePrice) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    // Phương thức lấy giá dịch vụ
    public double getServicePrice() {
        return servicePrice;
    }

    // Getter và Setter
    public String getServiceID() { return serviceID; }
    public String getServiceName() { return serviceName; }
}
