package com.ecorp.abhamburger;

import java.util.Date;

public class Customer extends Person{

    String email;
    String phone;
    String address;

    public Customer(String fName, String lName, Date birthday, String email, String phone, String address){
        super(fName, lName, birthday);
        this.email = email;
        this.phone = phone;
        this.address = address;

    }

}
