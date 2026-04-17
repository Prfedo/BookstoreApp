package com.bookstore.database;

import java.sql.*;

public class UserDAO {

    ConnectSQL db = new ConnectSQL();

    static ResultSet rs;
    static Connection r;
    static Statement ss;

    public void register(String Name, String Username, String email, String password) {
        try {
            r = db.connectSQLite();
            ss = r.createStatement();
            String query = "insert into Users(name,username,email,password)values('" + Name + "','" + Username + "','" + email + "','" + password + "')";
            ss.execute(query);
        } catch (SQLException ee) {
            System.out.println("Erron in Register: " + ee.getMessage());
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
                if (r != null) {
                    r.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void login(String emaill, String passwordd) {
        try {
            r = db.connectSQLite();
            ss = r.createStatement();
            String query = "select * from Users where email='" + emaill + "'and password ='" + passwordd + "'";
            rs = ss.executeQuery(query);
            if (rs.next()) {
                System.out.println("login Succesfully");
            } else {
                System.out.println("login failed");
            }
        } catch (SQLException ee) {
            System.out.println("Error in login: " + ee.getMessage());
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
                if (r != null) {
                    r.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void getUserById(int idd) {
        try {
            String query = "select* from Users where id=" + idd;
            r = db.connectSQLite();
            ss = r.createStatement();
            rs = ss.executeQuery(query);
            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name"));
            } else {
                System.out.println("Not Found");
            }
        } catch (SQLException ee) {
            System.out.println("Error in gerUserById: " + ee.getMessage());
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
                if (r != null) {
                    r.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
