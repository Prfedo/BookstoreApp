/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.database;

/**
 *
 * @author Admin
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:bookstore.db";

    public static String getURL() {
        return URL;
    }
}
