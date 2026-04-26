package com.bookstore.database;
import java.sql.*;

public class ConnectSQL {
    private final String URL = "jdbc:sqlite:bookstore.db"; //address
    
    public Connection connectSQLite() {
        Connection r = null;
        try{  
        r=DriverManager.getConnection(URL);
        }
        catch(SQLException e){
        System.out.println("Connection Error "+e.getMessage());
        }
        return r;
    }
}
