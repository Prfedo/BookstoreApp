package com.bookstore.controller;

import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private List<Order> orders;

    public OrderController() {
        orders = new ArrayList<>();
    }

    // Place a new order
    public Order placeOrder(int userId, List<CartItem> cartItems) {
        String date = LocalDate.now().toString();
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getSubtotal();
        }
        Order order = new Order(orders.size() + 1, userId, cartItems, totalPrice, date);
        orders.add(order);
        return order;
    }

    // Get orders by user
    public List<Order> getOrdersByUser(int userId) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUserId() == userId) result.add(o);
        }
        return result;
    }

    // Get all orders (admin)
    public List<Order> getAllOrders() {
        return orders;
    }
}