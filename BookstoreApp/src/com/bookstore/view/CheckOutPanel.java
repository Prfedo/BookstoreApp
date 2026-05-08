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

import java.awt.*;

import java.time.LocalDate;

import com.bookstore.model.CartItem;

import javax.swing.table.DefaultTableModel;

import java.util.List;

import java.util.ArrayList;

public class CheckOutPanel extends JPanel {
    
    private static final Color Beige        = new Color(245, 240, 232);
    private static final Color BROWN      = new Color(139, 69, 19);
    private static final Color BROWN_HOV  = new Color(160, 82, 45);
    private static final Color DARK_BROWN = new Color(59, 31, 10);
    
    private JTextField nameField, addressField, phoneField;
    
    private JComboBox<String> paymentBox;

    private JButton confirmBtn, backBtn;

    private JLabel totalLabel;

    private JPanel cardPanel;
    private JTextField cardNumberField, cardHolderField, cvvField;

    private CartController cartController;

    private OrderDAO orderDAO;
    
    public CheckOutPanel(CartController cartController) {
    this.cartController = cartController;
    this.orderDAO = new OrderDAO();
    this.setLayout(new BorderLayout());
    this.setBackground(Beige);

    JLabel title = new JLabel("Checkout");
    title.setFont(new Font("Georgia", Font.BOLD, 22));
    title.setForeground(DARK_BROWN);
    title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
    this.add(title, BorderLayout.NORTH);

    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setBackground(Beige);

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Beige);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    formPanel.setPreferredSize(new Dimension(550, 620));

    nameField    = new JTextField();
    addressField = new JTextField();
    phoneField   = new JTextField();
    addInputFilter(nameField, "letters");
    addInputFilter(phoneField, "numbers");
    String[] paymentOptions = {"Cash on Delivery", "Credit Card", "Debit Card"};
    paymentBox = new JComboBox<>(paymentOptions);

    JLabel summaryLabel = new JLabel("Order Summary");
    summaryLabel.setFont(new Font("Georgia", Font.BOLD, 15));
    summaryLabel.setForeground(DARK_BROWN);
    summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(summaryLabel);
    formPanel.add(Box.createVerticalStrut(8));

    String[] cols = {"Title", "Qty", "Subtotal"};
    DefaultTableModel summaryModel = new DefaultTableModel(cols, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    List<CartItem> items = cartController.getCartItems();
    for (int i = 0; i < items.size(); i++) {
        CartItem item = items.get(i);
        summaryModel.addRow(new Object[]{
            item.getBook().getTitle(),
            item.getQuantity(),
            String.format("$%.2f", item.getSubtotal())
        });
    }

    JTable summaryTable = new JTable(summaryModel);
    summaryTable.setFont(new Font("Arial", Font.PLAIN, 12));
    summaryTable.setRowHeight(25);
    summaryTable.setBackground(Beige);
    summaryTable.setForeground(DARK_BROWN);
    summaryTable.setEnabled(false);
    summaryTable.getTableHeader().setReorderingAllowed(false);
    summaryTable.getTableHeader().setResizingAllowed(false);
    summaryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    summaryTable.getTableHeader().setBackground(BROWN);
    summaryTable.getTableHeader().setForeground(Color.WHITE);

    JScrollPane summaryScroll = new JScrollPane(summaryTable);
    summaryScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    summaryScroll.setPreferredSize(new Dimension(490, 100));
    summaryScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
    summaryScroll.getViewport().setBackground(Beige);
    formPanel.add(summaryScroll);
    formPanel.add(Box.createVerticalStrut(10));

    JSeparator separator = new JSeparator();
    separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
    separator.setForeground(BROWN);
    separator.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(separator);
    formPanel.add(Box.createVerticalStrut(10));

    JLabel deliveryLabel = new JLabel("Delivery Details");
    deliveryLabel.setFont(new Font("Georgia", Font.BOLD, 15));
    deliveryLabel.setForeground(DARK_BROWN);
    deliveryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(deliveryLabel);
    formPanel.add(Box.createVerticalStrut(8));

    formPanel.add(FieldLabel("Full Name"));
    formPanel.add(StyledField(nameField));
    formPanel.add(Box.createVerticalStrut(8));

    formPanel.add(FieldLabel("Delivery Address"));
    formPanel.add(StyledField(addressField));
    formPanel.add(Box.createVerticalStrut(8));

    formPanel.add(FieldLabel("Phone Number"));
    formPanel.add(StyledField(phoneField));
    formPanel.add(Box.createVerticalStrut(8));

    formPanel.add(FieldLabel("Payment Method"));
    paymentBox.setBackground(Color.WHITE);
    paymentBox.setFont(new Font("Arial", Font.PLAIN, 13));
    paymentBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    paymentBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(paymentBox);
    formPanel.add(createCardPanel());
    formPanel.add(Box.createVerticalStrut(10));

    totalLabel = new JLabel("Order Total: $0.00");
    totalLabel.setFont(new Font("Georgia", Font.BOLD, 16));
    totalLabel.setForeground(DARK_BROWN);
    totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    formPanel.add(totalLabel);

    centerWrapper.add(formPanel);
    this.add(centerWrapper, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    bottomPanel.setBackground(Beige);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

    backBtn    = StyleButtons("Back to Cart");
    confirmBtn = StyleButtons("Confirm Order");

    bottomPanel.add(backBtn);
    bottomPanel.add(confirmBtn);
    this.add(bottomPanel, BorderLayout.SOUTH);

    updateTotal();
    setupActions();
    FieldNavigation();
}
        private JLabel FieldLabel (String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.BOLD, 13));

        label.setForeground(DARK_BROWN);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        return label;

    }
    
        private JTextField StyledField(JTextField field) {

        field.setFont(new Font("Arial", Font.PLAIN, 13));

        field.setBackground(Color.WHITE);

        field.setForeground(DARK_BROWN);

        field.setBorder(BorderFactory.createCompoundBorder(

            BorderFactory.createLineBorder(BROWN, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        return field;

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
        addHoverEffect(btn, BROWN, BROWN_HOV);
        return btn;
    }
        private void addHoverEffect(JButton btn, Color NormalColor, Color HoverColor) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(HoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(NormalColor);
            }
        });
    }
        private void updateTotal() {

        double total = cartController.getTotal();

        totalLabel.setText("Order Total: " + String.format("$%.2f", total));

    }
        private boolean validateFields() {
    if (nameField.getText().trim().isEmpty() ||
        addressField.getText().trim().isEmpty() ||
        phoneField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Please fill in all fields before confirming your order.");
        return false;
    }
    String selected = (String) paymentBox.getSelectedItem();
    if (selected.equals("Credit Card") || selected.equals("Debit Card")) {
        if (cardNumberField.getText().trim().isEmpty() ||
            cardHolderField.getText().trim().isEmpty() ||
            cvvField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in your card details.");
            return false;
        }
        
    }
    return true;
}
        private void FieldNavigation() {
    nameField.addActionListener(e -> addressField.requestFocus());
    addressField.addActionListener(e -> phoneField.requestFocus());
    phoneField.addActionListener(e -> paymentBox.requestFocus());
    paymentBox.addActionListener(e -> {
        String selected = (String) paymentBox.getSelectedItem();
        if (selected.equals("Credit Card") || selected.equals("Debit Card")) {
            cardNumberField.requestFocus();
        } 
    });
    cardNumberField.addActionListener(e -> cardHolderField.requestFocus());
    cardHolderField.addActionListener(e -> cvvField.requestFocus());
    
}
        private void setupActions() {

                        //Navigate between checkout and cart
        backBtn.addActionListener(e -> {
    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    CartPanel cartPanel = new CartPanel(cartController);
    parentFrame.setContentPane(cartPanel);
    parentFrame.revalidate();  //tells the frame to recalculate its layout with the new panel
    parentFrame.repaint();     //redraws the frame so the new panel appears on screen
     });
paymentBox.addActionListener(e -> {
    String selected = (String) paymentBox.getSelectedItem();
    boolean isCard = selected.equals("Credit Card") 
                  || selected.equals("Debit Card");
    cardPanel.setVisible(isCard);
    
    if (!isCard) {
        cardNumberField.setText("");
        cardHolderField.setText("");
        cvvField.setText("");
    }
    
    revalidate();
    repaint();
});
        confirmBtn.addActionListener(e -> {

            if (!validateFields()) return;


            // NOTE FOR MEMBER 5:
          // When login is implemented, SessionManager.currentUser will hold the logged-in user
           // Remove the else block below and keep only:
          // int userId = SessionManager.currentUser.getId();
          int userId;
           if (SessionManager.currentUser != null) {
            userId = SessionManager.currentUser.getId();
           } 
           else {
                 userId = 1; // TEMPORARY  remove when login is done
                }


            String date = LocalDate.now().toString();

            double total = cartController.getTotal();


            Order order = new Order(0,userId,cartController.getCartItems(),total,date);
                  //passed 0 in first argument as orderdao already assigns the user's ID

            try {
    orderDAO.saveOrder(order);
    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    List<CartItem> orderedItems = new ArrayList<>(cartController.getCartItems());
    cartController.clearCart();
    OrderConfirmDialogue confirmDialog = new OrderConfirmDialogue(
        parentFrame,
        order,
        orderedItems,
        nameField.getText().trim(),
        cartController
    );
    confirmDialog.setVisible(true);
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this,
        "Error placing order: " + ex.getMessage());
}
        });

    }    
        private JPanel createCardPanel() {
    cardPanel = new JPanel();
    cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
    cardPanel.setBackground(Beige);
    cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    cardPanel.setVisible(false);

    cardNumberField = new JTextField();
    cardHolderField = new JTextField();
    cvvField        = new JTextField();

    addInputFilter(cardHolderField, "letters");
    addInputFilter(cvvField, "numbers");
    cvvField.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyTyped(java.awt.event.KeyEvent e) {
        if (cvvField.getText().length() >= 3) {
            e.consume();
        }
    }
});
    addInputFilter(cardNumberField,"numbers");
    cardNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyTyped(java.awt.event.KeyEvent e) {
        if (cardNumberField.getText().length() >= 16) {
            e.consume();
        }
    }
});
    
    cardPanel.add(Box.createVerticalStrut(8));
    cardPanel.add(FieldLabel("Card Number"));
    cardPanel.add(StyledField(cardNumberField));
    cardPanel.add(Box.createVerticalStrut(8));
    cardPanel.add(FieldLabel("Card Holder Name"));
    cardPanel.add(StyledField(cardHolderField));
    cardPanel.add(Box.createVerticalStrut(8));
    cardPanel.add(FieldLabel("CVV"));
    cvvField.setMaximumSize(new Dimension(100, 35));
    cardPanel.add(StyledField(cvvField));

    return cardPanel;
}
           //for fields to accept only letters or numbers
    private void addInputFilter(JTextField field, String type) {
    field.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent e) {
            char c = e.getKeyChar();
            if (type.equals("letters")) {
                if (!Character.isLetter(c) && c != ' ') {
                    e.consume();
                }
            } else if (type.equals("numbers")) {
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        }
    });
}
  }


  
