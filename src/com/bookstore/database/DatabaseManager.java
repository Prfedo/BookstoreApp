package com.bookstore.database;

import java.sql.*;

public class DatabaseManager {

    static Connection c;
    static Statement ss;
    static String queryTable1, qTable2, qTable3, qTable4;

    public static void createTabels() {
        //book,users,orders,order_item
        ConnectSQL c1 = new ConnectSQL();
        try {
            c = c1.connectSQLite();
            ss = c.createStatement();
            queryTable1 = "create table if not exists Books(ID integer not null primary key autoincrement,Title Text not null,"
                    + " Author Text not null,Genre Text not null,Price real not null"
                    + ",Stock integer not null,Cover Text not null)";

            qTable2 = "create table if not exists Users(ID integer not null primary key autoincrement,username varchar(20) not null"
                    + ",email varchar(255) not null unique,password varchar(30) not null,"
                    + "Name varchar(30) not null,is_admin integer default 0) ";

            qTable3 = "create table if not exists Order_item(ID integer not null primary key autoincrement,order_id integer not null,"
                    + "book_id integer not null,quantity integer not null, price real not null"
                    + ",foreign key(order_id) references Orders(ID),"
                    + "foreign key(book_id) references Books(ID))";

            qTable4 = "create table if not exists Orders(ID integer not null primary key autoincrement,user_id integer not null,"
                    + "total_price real not null,created_at Text not null,"
                    + "foreign key(user_id) references Users(ID))";

            ss.execute("PRAGMA foreign_keys = ON");
            ss.execute(queryTable1);
            ss.execute(qTable2);
            ss.execute(qTable4);
            ss.execute(qTable3);
        } catch (SQLException ee) {
            System.out.println("Error creating tables: " + ee.getMessage());
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static void closeAll(Connection c, Statement ss, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException ignored) {
        }
    }
}