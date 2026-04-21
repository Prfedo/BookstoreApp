package com.bookstore.view;

import com.bookstore.model.Book;
import javax.swing.*;
import java.awt.*;

public class BookDetailDialog extends JDialog {

    public BookDetailDialog(JFrame parent, Book book) {
        this(parent, new String[]{
            book.getTitle(),
            book.getAuthor(),
            book.getGenre(),
            String.format("$%.2f", book.getPrice()),
            book.getCover()
        });
    }

    public BookDetailDialog(JFrame parent, String[] book) {
        super(parent, book[0], true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 240, 232));

        // BOOK INFO
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 240, 232));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(book[0]);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(new Color(59, 31, 10));

        JLabel authorLabel = new JLabel("by " + book[1]);
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        authorLabel.setForeground(new Color(130, 130, 130));

        JLabel priceLabel = new JLabel(book[3]);
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
            JOptionPane.showMessageDialog(this,
                qty + " copy of \"" + book[0] + "\" added to cart!",
                "Added to Cart",
                JOptionPane.INFORMATION_MESSAGE);
            // TODO: Member 4 - CartPanel.addBook(book[0], book[3], qty);
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