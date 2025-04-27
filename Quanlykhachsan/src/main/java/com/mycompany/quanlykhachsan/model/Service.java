package com.mycompany.quanlykhachsan.model;

public class Service {
    private String serviceID;
    private String serviceName;
    private double servicePrice;
    private int quantityInStock;

    public Service() {
    }
    
    
    // Constructor đầy đủ
    public Service(String serviceID, String serviceName, double servicePrice, int quantityInStock) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.quantityInStock = quantityInStock;
    }

    // Constructor mới không cần quantityInStock
    public Service(String serviceID, String serviceName, double servicePrice) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.quantityInStock = 0; // Mặc định số lượng tồn kho là 0
    }

    // Các getter và setter
    public double getServicePrice() {
        return servicePrice;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceID='" + serviceID + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}