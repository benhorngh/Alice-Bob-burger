package com.ecorp.abhamburger;

import java.util.Date;

public class Customer extends Person{

    String phone;
    String address;
    Date birthday;
    String orderId;


    public Customer(String fName, String lName, String email,String password,Date birthday, String phone, String address){
        super(fName, lName, email, password);
        this.phone = phone;
        this.address = address;
        this.birthday = birthday;

    }
    public Customer(){}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
