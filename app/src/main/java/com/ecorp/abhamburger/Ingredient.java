package com.ecorp.abhamburger;

public class Ingredient {
    static int idGenerator = 1111;

    String name;
    int id;

    public Ingredient(String name) {
        this.name = name;
        id = ++idGenerator;
    }
    public Ingredient(){}
}
