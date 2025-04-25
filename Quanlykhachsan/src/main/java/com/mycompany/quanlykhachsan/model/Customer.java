package com.mycompany.quanlykhachsan.model;

import java.math.BigDecimal;

public class Customer {
    private String CCCD;
    private String Ten;
    private String sdt;
    private String gioitinh;  

    public Customer(String CCCD, String Ten, String sdt, String gioitinh) {
        this.CCCD = CCCD;
        this.Ten = Ten;
        this.sdt = sdt;
        this.gioitinh = gioitinh;
    }
    
    public void display() {
        System.out.println(CCCD+" "+BigDecimal.TEN +" "+sdt+" "+gioitinh);
    }
    
}
