package com.bookstore.view;

import com.bookstore.model.Book;
import com.bookstore.controller.CartController; // [ADDED] needed to actually add books to cart
import javax.swing.*;
import java.awt.*;

public class BookDetailDialog extends JDialog {

    // [ADDED] cartController field so we can call addToCart
    private CartController cartController;

    // [MODIFIED] constructor now accepts CartController so the dialog can add to cart
    public BookDetailDialog(JFrame parent, Book book, CartController cartController) {
        super(parent, book.getTitle(), true);
        this.cartController = cartController;
        buildUI(
            book.getTitle(),
            book.getAuthor(),
            book.getGenre(),
            String.format("$%.2f", book.getPrice()),
            book,           // [ADDED] pass the full Book object so addToCart gets it
            null
        );
    }

    // [KEPT] string-array constructor retained for backward compatibility,
    //        but it cannot add to cart (no Book id / price available as a Book object)
    //        — callers should migrate to the Book constructor above.
    public BookDetailDialog(JFrame parent, String[] bookArr) {
        super(parent, bookArr[0], true);
        buildUI(bookArr[0], bookArr[1], bookArr[2], bookArr[3], null, bookArr);
    }

    // [EXTRACTED] shared UI builder to avoid duplicating Swing code
    private void buildUI(String title, String author, String genre,
                         String price, Book book, String[] bookArr) {
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 240, 232));

        // BOOK INFO
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 240, 232));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(new Color(59, 31, 10));

        JLabel authorLabel = new JLabel("by " + author);
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        authorLabel.setForeground(new Color(130, 130, 130));

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(139, 69, 19));

        // QUANTITY SELECTOR
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        qtyPanel.setBackground(new Color(245, 240, 232));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
        JSpinner qtySpinner = new JSpinner(spinnerModel);
        qtySpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        qtySpinner.setPreferredSize(new Dimension(60, 30));

        qtyPanel.add(qtyLabel);
        qtyPanel.add(qtySpinner);

        // ADD TO CART BUTTON
        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBackground(new Color(139, 69, 19));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> {
            int qty = (int) qtySpinner.getValue();

            // [FIXED] was a TODO stub — now actually calls cartController.addToCart()
            //         only works when the Book object is available (not the legacy String[] path)
            if (cartController != null && book != null) {
                cartController.addToCart(book, qty);
            }
            // [KEPT] confirmation dialog so user knows the action worked
            JOptionPane.showMessageDialog(this,
                qty + " copy of \"" + title + "\" added to cart!",
                "Added to Cart",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(qtyPanel);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(addBtn);

        add(infoPanel, BorderLayout.CENTER);
    }
}