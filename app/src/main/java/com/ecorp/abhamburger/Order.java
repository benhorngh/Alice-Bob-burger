package com.ecorp.abhamburger;

import java.util.List;

public class Order {
    int OrderID;
    String CustomerId;
    List<Integer> dishesId;
    String notes;
    double total;
    boolean delivery;


    public Order(int orderID, String customerId, List<Integer> dishesId, String notes, double total, boolean delivery) {
        OrderID = orderID;
        CustomerId = customerId;
        this.dishesId = dishesId;
        this.notes = notes;
        this.total = total;
        this.delivery = delivery;
    }

    public Order() {}

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public List<Integer> getDishesId() {
        return dishesId;
    }

    public void setDishesId(List<Integer> dishesId) {
        this.dishesId = dishesId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }
}