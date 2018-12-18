package com.ecorp.abhamburger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Person {

    static int idGenerator = 1111;

    int emoplyeeID;
    double salary = 4000;
    String type = "seller";
    List<Integer> orderID;

    public Employee(String fName, String lName, String email,String password, Date bday,  int[] orderID) {
        super(fName, lName, email, password, bday);
        this.emoplyeeID = idGenerator ++;
        this.orderID = new ArrayList<Integer>();
    }
}
