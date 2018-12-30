package com.ecorp.abhamburger;

import java.util.Date;
import java.util.List;

public class Order {
    private String OrderID;
    private String CustomerId;
    private List<Integer> dishesId;
    private String notes;
    private double total;
    private String status;
    private boolean delivery;
    private Date time;


    public Order(String customerId, List<Integer> dishesId, String notes, double total, boolean delivery, String status, Date time) {
        CustomerId = customerId;
        this.dishesId = dishesId;
        this.notes = notes;
        this.total = total;
        this.delivery = delivery;
        this.status = status;
        this.time = time;
    }

    public Order() {}

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}