package com.ecorp.abhamburger;

import java.util.Date;

public class Customer extends Person{

    String phone;
    String address;


    public Customer(String fName, String lName, String email,String password,Date birthday, String phone, String address){
        super(fName, lName, email, password, birthday);
        this.phone = phone;
        this.address = address;

    }

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



}
