package com.bookstore.database;

import com.bookstore.model.User;
import java.sql.*;

public class UserDAO {

    ConnectSQL db = new ConnectSQL();
    static ResultSet rs;
    static Connection r;
    static Statement ss;

    public boolean register(String Name, String Username, String email, String password) {
        try {
            r = db.connectSQLite();
            ss = r.createStatement();
            String query = "insert into Users ( name, username , email , password ) values ( '" + Name + "','" + Username + "','" + email + "','" + password + "')";
            ss.execute(query);
            return true;
        } catch (SQLException ee) {
            System.out.println("Erron in Register: " + ee.getMessage());
            return false;
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

    public User login(String emailOrUsername, String passwordd) {
        try {
            r = db.connectSQLite();
            ss = r.createStatement();
            String query = "select * from Users where ( email='" + emailOrUsername + "' OR username='" + emailOrUsername + "') and password ='" + passwordd + "'";
            rs = ss.executeQuery(query);
            if (rs.next()) {
                //int id, String name, String email, String password, boolean isAdmin)
                User user = new User(rs.getInt("ID"), rs.getString("Name"), rs.getString("username"),
                        rs.getString("email"), rs.getString("password"),
                        rs.getBoolean("is_admin"));

                System.out.println("login Succesfully");
                return user;
            } else {
                System.out.println("login failed");
                return null;
            }
        } catch (SQLException ee) {
            System.out.println("Error in login: " + ee.getMessage());
            return null;
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

    public User getUserById(int idd) {
        try {
            String query = "select* from Users where id=" + idd;
            r = db.connectSQLite();
            ss = r.createStatement();
            rs = ss.executeQuery(query);
            if (rs.next()) {
                User user = new User(rs.getInt("ID"), rs.getString("Name"), rs.getString("username"),
                        rs.getString("email"), rs.getString("password"),
                        rs.getBoolean("is_admin"));
                return user;
                //System.out.println("Name: " + rs.getString("name"));
            } else {
                System.out.println("Not Found");
                return null;
            }
        } catch (SQLException ee) {
            System.out.println("Error in gerUserById: " + ee.getMessage());
            return null;
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
                return null;
            }
        }
    }
}
