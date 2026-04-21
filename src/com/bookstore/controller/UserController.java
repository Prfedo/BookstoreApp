package com.bookstore.controller;

import com.bookstore.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users;

    public UserController() {
        users = new ArrayList<>();
    }

    // Register a new user
    public boolean register(String name, String username, String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return false; // email already exists
            }
        }
        User newUser = new User(users.size() + 1, name,username, email, password, false);
        users.add(newUser);
        return true;
    }

    // Login
    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                u.getPassword().equals(password)) {
                return u; // login success
            }
        }
        return null; // login failed
    }

    // Get user by id
    public User getUserById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }
}