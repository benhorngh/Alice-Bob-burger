package com.ecorp.abhamburger;

import java.util.List;

public class Dish {
    int id;
    String name;
    String description;
    List<Ingredient> ingredients ;
    double price;


    public Dish(){}
    
    public Dish(int id, String name, String description, List<Ingredient> ingredients, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
