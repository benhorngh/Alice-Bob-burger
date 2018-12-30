package com.ecorp.abhamburger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Person {


    double salary;
    String type;

    public Employee(String fName, String lName, String email,String password) {
        super(fName, lName, email, password);
        this.salary = 4000;
        this.type = "seller";
    }
    public Employee(){
        this.salary = 4000;
        this.type = "seller";
    }
}
