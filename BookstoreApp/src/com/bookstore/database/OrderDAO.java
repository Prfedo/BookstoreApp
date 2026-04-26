package com.bookstore.database;

import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Order Data Accses Object
public class OrderDAO {

    ConnectSQL db = new ConnectSQL();
    static Connection c;
    static Statement ss;
    static ResultSet rs;

    public void saveOrder(Order order) throws SQLException {
        c = db.connectSQLite();
        ss = c.createStatement();
        String query = "insert into Orders (user_id, total_price, created_at) values ( " + order.getUserId() + "," + order.getTotalPrice() + ",'" + order.getDate() + "')";
        ss.execute(query);
        // Step 2: get the auto-generated id SQLite just gave it
        rs = ss.executeQuery("select last_insert_rowid() as id");
        int orderId = rs.getInt("id");

        for (int i = 0; i < order.getItems().size(); i++) {
            CartItem item = order.getItems().get(i);
            String itemQuery = "insert into Order_item (order_id, book_id, quantity, price) values (" + orderId + "," + item.getBook().getId() + "," + item.getQuantity() + "," + item.getSubtotal() + ")";
            ss.execute(itemQuery);
        }
        DatabaseManager.closeAll(c, ss, rs);
    }

    public List<Order> getOrdersByUserId(int id) throws SQLException {
        String query = "select * from Orders where user_id =" + id + " order by id desc";
        c = db.connectSQLite();
        ss = c.createStatement();
        rs = ss.executeQuery(query);
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            //    public Order(int id, int userId, List<CartItem> items, String date) {

            Order order = new Order(rs.getInt("ID"), rs.getInt("user_id"), new ArrayList<>(), rs.getDouble("total_price"), rs.getString("created_at"));
            orders.add(order);
        }
        DatabaseManager.closeAll(c, ss, rs);
        return orders;
    }
}
