package com.ecorp.abhamburger;

import java.util.Date;

public class Manager extends Person{
    int id;
    static int idGenerator = 1;

    public Manager(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        id = idGenerator++;
    }




}
