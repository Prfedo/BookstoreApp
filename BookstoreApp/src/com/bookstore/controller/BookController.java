package com.bookstore.controller;

import com.bookstore.model.Book;
import java.util.List;
import java.util.ArrayList;

public class BookController {
    private List<Book> books;

    public BookController() {
        books = new ArrayList<>();
    }

    // Get all books
    public List<Book> getAllBooks() {
        return books;
    }

    // Add a book
    public void addBook(Book book) {
        books.add(book);
    }

    // Delete a book by id
    public void deleteBook(int id) {
        books.removeIf(b -> b.getId() == id);
    }

    // Search books by title
    public List<Book> searchByTitle(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    // Search books by genre
    public List<Book> searchByGenre(String genre) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getGenre().equalsIgnoreCase(genre)) {
                result.add(b);
            }
        }
        return result;
    }
}