package com.ecorp.abhamburger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Person implements Comparable<Employee>{


    double salary;
    String type;
    int actions;

    public Employee(String fName, String lName, String email,String password) {
        super(fName, lName, email, password);
        this.salary = 4000;
        this.type = "seller";
        this.actions = 0;
    }
    public Employee(){
        this.salary = 4000;
        this.type = "seller";
    }


    @Override
      public int compareTo(Employee other) {
        return this.actions - other.actions;
    }
}
