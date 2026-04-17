package com.bookstore.database;

import java.sql.*;

public class BookDAO {
    static Connection c;
    static Statement ss;
    static ResultSet rs;
    ConnectSQL db =new ConnectSQL();
    
    public void getAllBooks()
    {
        try{
        c=db.connectSQLite();
        ss=c.createStatement();
        String query="select * from books order by id";
        }
        catch(SQLException ee)
        {
            System.out.println("getAllBooks error "+ee.getMessage());
        }
    } 
    public void addBook(String title){
        
    } 
    public void updateBook(){
    }
    public void deleteBook(){
    } 
    public void searchBooks(String title){
    }
}
