package com.ecorp.abhamburger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Person {


    double salary = 4000;
    String type = "seller";
    List<Integer> orderID;

    public Employee(String fName, String lName, String email,String password) {
        super(fName, lName, email, password);
        this.orderID = new ArrayList<Integer>();
    }
}
