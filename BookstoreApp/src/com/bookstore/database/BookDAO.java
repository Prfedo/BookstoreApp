package com.bookstore.database;

import com.bookstore.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Book Data Access Object
public class BookDAO {

    static Connection c;
    static Statement ss;
    static ResultSet rs;
    ConnectSQL db = new ConnectSQL();

    public List<Book> getAllBooks() throws SQLException {
        List<Book> b = new ArrayList<>();
        try {
            c = db.connectSQLite();
            ss = c.createStatement();
            String query = "select * from books order by id";
            rs = ss.executeQuery(query);
            while (rs.next()) {
                b.add(new Book(rs.getInt("ID"), rs.getString("Title"),
                        rs.getString("Author"), rs.getString("Genre"),
                        rs.getDouble("Price"), rs.getInt("stock"),
                        rs.getString("Cover")));
            }
        } catch (SQLException ee) {
            System.out.println("getAllBooks error " + ee.getMessage());
        } finally {
            DatabaseManager.closeAll(c, ss, rs);
        }
        return b;
    }

    public void addBook(Book book) {
        try {
            c = db.connectSQLite();
            ss = c.createStatement();
            String query = "insert into Books ( Title, Author , Genre , Price , "
                    + "Stock ,Cover ) values ( '" + book.getTitle() + "','" + book.getAuthor()
                    + "','" + book.getGenre() + "'," + book.getPrice() + "," + book.getStock()
                    + ",'" + book.getCover() + "')";
            ss.execute(query);
        } catch (SQLException ee) {
            System.out.println("addBook error " + ee.getMessage());
        } finally {
            DatabaseManager.closeAll(c, ss, rs);
        }
    }

    public void updateBook(Book book) {
        try {
            c = db.connectSQLite();
            ss = c.createStatement();
            String query = "update Books set Title = '" + book.getTitle()
                    + "' , Author = '" + book.getAuthor() + "' , Genre = '"
                    + book.getGenre() + "' , Price = " + book.getPrice() + " , Stock ="
                    + book.getStock() + ",Cover = '" + book.getCover() + "'"
                    + " where id=" + book.getId();
            ss.execute(query);
        } catch (SQLException ee) {
            System.out.println("updateBook error " + ee.getMessage());
        } finally {
            DatabaseManager.closeAll(c, ss, rs);
        }
    }

    public void deleteBook(int id) {
        try {
            c = db.connectSQLite();
            ss = c.createStatement();
            String query = "delete from Books where id ='" + id + "'";
            ss.execute(query);
        } catch (SQLException ee) {
            System.out.println("deleteBook error " + ee.getMessage());
        } finally {
            DatabaseManager.closeAll(c, ss, rs);
        }
    }

    public List<Book> searchBooks(String title) {
        List<Book> b = new ArrayList<>();
        try {
            c = db.connectSQLite();
            ss = c.createStatement();
            String query = "select * from Books where Title like '%" + title + "%' order by id";
            rs = ss.executeQuery(query);
            while (rs.next()) {
                b.add(new Book(rs.getInt("ID"), rs.getString("Title"),
                        rs.getString("Author"), rs.getString("Genre"),
                        rs.getDouble("Price"), rs.getInt("stock"),
                        rs.getString("Cover")));
            }
        } catch (SQLException ee) {
            System.out.println("getAllBooks error " + ee.getMessage());
        } finally {
            DatabaseManager.closeAll(c, ss, rs);
        }
        return b;
    }
}
