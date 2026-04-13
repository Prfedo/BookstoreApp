package com.bookstore.database;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:bookstore.db";

    public static String getURL() {
        return URL;
    }
}
