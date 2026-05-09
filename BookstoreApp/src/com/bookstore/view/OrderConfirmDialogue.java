/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.view;

/**
 *
 * @author bassel saeed
 */
import com.bookstore.model.Order;
import com.bookstore.model.CartItem;
import com.bookstore.controller.CartController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.bookstore.model.SessionManager; 

public class OrderConfirmDialogue extends JDialog {

    private static final Color Beige      = new Color(245, 240, 232);
    private static final Color BROWN      = new Color(139, 69, 19);
    private static final Color BROWN_HOV  = new Color(160, 82, 45);
    private static final Color DARK_BROWN = new Color(59, 31, 10);

    public OrderConfirmDialogue(JFrame parent, Order order,
            List<CartItem> items, String customerName,
            CartController cartController) {

        super(parent, "Order Confirmed", true);
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(Beige);
        setLayout(new BorderLayout());

        // NORTH — success title
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Beige);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel successLabel = new JLabel("Order Placed Successfully!");
        successLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        successLabel.setForeground(DARK_BROWN);
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel thankLabel = new JLabel("Thank you, " + customerName + "!");
        thankLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        thankLabel.setForeground(new Color(130, 130, 130));
        thankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + order.getDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dateLabel.setForeground(DARK_BROWN);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(successLabel);
        topPanel.add(Box.createVerticalStrut(6));
        topPanel.add(thankLabel);
        topPanel.add(Box.createVerticalStrut(4));
        topPanel.add(dateLabel);

        this.add(topPanel, BorderLayout.NORTH);

        // CENTER — items table
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Beige);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel itemsLabel = new JLabel("Items Ordered");
        itemsLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        itemsLabel.setForeground(DARK_BROWN);
        itemsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(itemsLabel);
        centerPanel.add(Box.createVerticalStrut(8));

        String[] cols = {"Title", "Qty", "Subtotal"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            tableModel.addRow(new Object[]{
                item.getBook().getTitle(),
                item.getQuantity(),
                String.format("$%.2f", item.getSubtotal())
            });
        }

        JTable itemsTable = new JTable(tableModel);
        itemsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemsTable.setRowHeight(25);
        itemsTable.setBackground(Beige);
        itemsTable.setForeground(DARK_BROWN);
        itemsTable.setEnabled(false);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        itemsTable.getTableHeader().setResizingAllowed(false);
        itemsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        itemsTable.getTableHeader().setBackground(BROWN);
        itemsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(itemsTable);
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableScroll.setPreferredSize(new Dimension(440, 150));
        tableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        tableScroll.getViewport().setBackground(Beige);
        centerPanel.add(tableScroll);
        centerPanel.add(Box.createVerticalStrut(15));

        JLabel totalLabel = new JLabel("Total Paid: " +
                String.format("$%.2f", order.getTotalPrice()));
        totalLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        totalLabel.setForeground(DARK_BROWN);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(totalLabel);

        this.add(centerPanel, BorderLayout.CENTER);

        // SOUTH — close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Beige);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 20, 30));

        JButton closeBtn = StyleButtons("Continue Shopping");
        closeBtn.addActionListener(e -> {
    dispose();
    CatalogPanel catalogPanel = new CatalogPanel(SessionManager.currentUser);
    parent.setContentPane(catalogPanel);
    parent.revalidate();
    parent.repaint();
});

        bottomPanel.add(closeBtn);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton StyleButtons(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BROWN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 35));
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