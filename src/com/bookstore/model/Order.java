package com.bookstore.model;

import java.util.List;

public class Order {
    private int id;
    private int userId;
    private List<CartItem> items;
    private double totalPrice;
    private String date;

    public Order(int id, int userId, List<CartItem> items, String date) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.date = date;
        this.totalPrice = calculateTotal();
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getId()                  { return id; }
    public int getUserId()              { return userId; }
    public List<CartItem> getItems()    { return items; }
    public double getTotalPrice()       { return totalPrice; }
    public String getDate()             { return date; }
}
