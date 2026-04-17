package com.bookstore.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private double price;
    private int stock;

    public Book(int id, String title, String author, String genre, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.stock = stock;
    }

    public int getId()          { return id; }
    public String getTitle()    { return title; }
    public String getAuthor()   { return author; }
    public String getGenre()    { return genre; }
    public double getPrice()    { return price; }
    public int getStock()       { return stock; }

    public void setTitle(String title)   { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre)   { this.genre = genre; }
    public void setPrice(double price)   { this.price = price; }
    public void setStock(int stock)      { this.stock = stock; }
}
