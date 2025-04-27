package com.mycompany.quanlykhachsan.model;

public class ServiceUsage {
    private Customer customer;
    private Service service;
    private int quantity;

    public ServiceUsage() {}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}