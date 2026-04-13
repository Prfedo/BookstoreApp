package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartController {
    private List<CartItem> cartItems;

    public CartController() {
        cartItems = new ArrayList<>();
    }

    // Add book to cart
    public void addToCart(Book book, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(book, quantity));
    }

    // Remove book from cart
    public void removeFromCart(int bookId) {
        cartItems.removeIf(item -> item.getBook().getId() == bookId);
    }

    // Get all cart items
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Calculate total price
    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Clear the cart
    public void clearCart() {
        cartItems.clear();
    }
}