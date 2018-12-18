package com.ecorp.abhamburger;

import java.util.Date;

public class Manager extends Person{
    int id;
    static int idGenerator = 1;

    public Manager(String firstName, String lastName, String email, String password, Date birthday) {
        super(firstName, lastName, email, password, birthday);
        id = idGenerator++;
    }




}
