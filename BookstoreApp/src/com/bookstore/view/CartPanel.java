package com.bookstore.view;
import com.bookstore.controller.CartController;
import com.bookstore.model.CartItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class CartPanel extends JPanel {
    private static final Color Beige        = new Color(245, 240, 232);
    private static final Color BROWN      = new Color(139, 69, 19);
    private static final Color BROWN_HOV  = new Color(160, 82, 45);
    private static final Color DARK_BROWN = new Color(59, 31, 10);
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton removeBtn, clearBtn, checkoutBtn;
    private CartController cartController;
    public CartPanel(CartController cartController) {
        this.cartController = cartController;

        this.setLayout(new BorderLayout());
        this.setBackground(Beige);
        JLabel title = new JLabel("Your Cart");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(DARK_BROWN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        this.add(title, BorderLayout.NORTH);
        String[] columns = {"Title", "Author", "Qty", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; } //so user cant override
        };
        cartTable = new JTable(tableModel);
        cartTable.getTableHeader().setReorderingAllowed(false);
        cartTable.getTableHeader().setResizingAllowed(false);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.setRowHeight(28);
        cartTable.setBackground(Beige);
        cartTable.setForeground(DARK_BROWN);
        cartTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        cartTable.getTableHeader().setBackground(BROWN);
        cartTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(
                0, 30, 0, 30));
        scrollPane.getViewport().setBackground(Beige);
        this.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Beige);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                15, 30, 20, 30));
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
    private int lastRow = -1;
    public void mousePressed(java.awt.event.MouseEvent e) {
        int row = cartTable.rowAtPoint(e.getPoint());
        if (row == lastRow) {
            cartTable.clearSelection();
            lastRow = -1;
        } else {
            lastRow = row;
        }
    }
});
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        totalLabel.setForeground(DARK_BROWN);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Beige);

        removeBtn  = StyleButtons("Remove Selected");
        clearBtn   = StyleButtons("Clear Cart");
        checkoutBtn = StyleButtons("Proceed to Checkout");

        btnPanel.add(removeBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(checkoutBtn);

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);

        ButtonActions();
        refreshTable();
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
    public void refreshTable() {
        tableModel.setRowCount(0); // clears all rows
        List<CartItem> items = cartController.getCartItems();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            String title    = item.getBook().getTitle();
            String author   = item.getBook().getAuthor();
            String qty      = String.valueOf(item.getQuantity());
            String subtotal = String.format("$%.2f", item.getSubtotal());

            String[] row = {title, author, qty, subtotal};
            tableModel.addRow(row);
        }
        totalLabel.setText("Total: " + String.format("$%.2f", cartController.getTotal()));
    }
    private void ButtonActions() {

        removeBtn.addActionListener(e -> {
    
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a row to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
        "Remove the selected item(s)?", "Confirm",
        JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
            int bookId = cartController.getCartItems().get(row).getBook().getId();
            cartController.removeFromCart(bookId);
            refreshTable();
    }
        });
    

        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear entire cart?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cartController.clearCart();
                refreshTable();
            }
        });
                //Navigate between checkout and cart
        checkoutBtn.addActionListener(e -> {
    if (cartController.getCartItems().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Your cart is empty!");
        return;
    }
    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    CheckOutPanel checkoutPanel = new CheckOutPanel(cartController);
    parentFrame.setContentPane(checkoutPanel);
    parentFrame.revalidate(); //tells the frame to recalculate its layout with the new panel
    parentFrame.repaint();    //redraws the frame so the new panel appears on screen
});
    }
}
