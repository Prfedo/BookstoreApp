/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.view;

/**
 *
 * @author bassel saeed
 */
import com.bookstore.model.SessionManager;

import com.bookstore.controller.CartController;

import com.bookstore.database.OrderDAO;

import com.bookstore.model.Order;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.util.List;

import java.sql.SQLException;

public class OrderHistoryPanel extends JPanel {
    
    private static final Color Beige      = new Color(245, 240, 232);

    private static final Color BROWN      = new Color(139, 69, 19);

    private static final Color BROWN_HOV  = new Color(160, 82, 45);

    private static final Color DARK_BROWN = new Color(59, 31, 10);


    private JTable ordersTable;

    private DefaultTableModel tableModel;

    private JLabel statusLabel;

    private JButton backBtn;


    private OrderDAO orderDAO;

    private CartController cartController;
    
    public OrderHistoryPanel(CartController cartController) {

        this.cartController = cartController;

        this.orderDAO = new OrderDAO();

        this.setLayout(new BorderLayout());

        this.setBackground(Beige);
        
        JLabel title = new JLabel("My Orders");

        title.setFont(new Font("Georgia", Font.BOLD, 22));

        title.setForeground(DARK_BROWN);

        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        this.add(title, BorderLayout.NORTH);
        
        String[] columns = {"Order ID", "Date", "Total Price", "Status"};

        tableModel = new DefaultTableModel(columns, 0) {

            public boolean isCellEditable(int r, int c) { return false; }

        };


        ordersTable = new JTable(tableModel);

        ordersTable.setFont(new Font("Arial", Font.PLAIN, 13));

        ordersTable.setRowHeight(30);

        ordersTable.setBackground(Beige);

        ordersTable.setForeground(DARK_BROWN);

        ordersTable.setSelectionBackground(new Color(210, 180, 140));

        ordersTable.setSelectionForeground(DARK_BROWN);

        ordersTable.getTableHeader().setReorderingAllowed(false);

        ordersTable.getTableHeader().setResizingAllowed(false);

        ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        ordersTable.getTableHeader().setBackground(BROWN);

        ordersTable.getTableHeader().setForeground(Color.WHITE);


        JScrollPane scrollPane = new JScrollPane(ordersTable);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        scrollPane.getViewport().setBackground(Beige);

        this.add(scrollPane, BorderLayout.CENTER);
        
        statusLabel = new JLabel("");

        statusLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        statusLabel.setForeground(new Color(130, 130, 130));

        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));


        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.setBackground(Beige);

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));


        backBtn = StyleButtons("Back to Shop");


        bottomPanel.add(statusLabel, BorderLayout.WEST);

        bottomPanel.add(backBtn, BorderLayout.EAST);

        this.add(bottomPanel, BorderLayout.SOUTH);


        loadOrders();

        setupActions();

    }
    
    private void loadOrders() {

        tableModel.setRowCount(0);


        // NOTE FOR MEMBER 5:

        // Replace userId = 1 with:

        // int userId = SessionManager.currentUser.getId();

        // after login is implemented

        int userId;

        if (SessionManager.currentUser != null) {

            userId = SessionManager.currentUser.getId();

        } else {

            userId = 1; // TEMPORARY — remove when login is done

        }


        try {

            List<Order> orders = orderDAO.getOrdersByUserId(userId);


            if (orders.isEmpty()) {

                statusLabel.setText("No orders found.");

                return;

            }


            for (int i = 0; i < orders.size(); i++) {

                Order order = orders.get(i);

                tableModel.addRow(new Object[]{

                    order.getId(),

                    order.getDate(),

                    String.format("$%.2f", order.getTotalPrice()),

                    "Completed"

                });

            }


        } catch (SQLException ex) {

            statusLabel.setText("Error loading orders: " + ex.getMessage());

        }

    }
    
    private void setupActions() {

        backBtn.addActionListener(e -> {

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            CatalogPanel catalogPanel = new CatalogPanel();

            parentFrame.setContentPane(catalogPanel);

            parentFrame.revalidate();

            parentFrame.repaint();

        });

    }


    private JButton StyleButtons(String text) {

        JButton btn = new JButton(text);

        btn.setBackground(BROWN);

        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Arial", Font.BOLD, 12));

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(160, 35));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent e) {

                btn.setBackground(BROWN_HOV);

            }

            public void mouseExited(java.awt.event.MouseEvent e) {

                btn.setBackground(BROWN);

            }

        });

        return btn;

    }

}


