package com.bookstore.view;

import com.bookstore.controller.CartController;
import com.bookstore.model.CartItem;
// [ADDED] import — needed so backBtn can read SessionManager.currentUser
// and pass it to CatalogPanel so the navbar stays logged-in after navigating back.
import com.bookstore.model.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartPanel extends JPanel {
    private static final Color Beige     = new Color(245, 240, 232);
    private static final Color BROWN     = new Color(139, 69, 19);
    private static final Color BROWN_HOV = new Color(160, 82, 45);
    private static final Color DARK_BROWN= new Color(59, 31, 10);

    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    // [CHANGED] Added backBtn to the field list.
    // Previously: private JButton removeBtn, clearBtn, checkoutBtn;
    // Now:        private JButton removeBtn, clearBtn, checkoutBtn, backBtn;
    private JButton removeBtn, clearBtn, checkoutBtn, backBtn;

    private CartController cartController;

    public CartPanel(CartController cartController) {
        this.cartController = cartController;

        this.setLayout(new BorderLayout());
        this.setBackground(Beige);

        // ── title (unchanged) ────────────────────────────────────────────────
        JLabel title = new JLabel("Your Cart");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(DARK_BROWN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        this.add(title, BorderLayout.NORTH);

        // ── table (unchanged) ─────────────────────────────────────────────────
        String[] columns = {"Title", "Author", "Qty", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(tableModel);
        cartTable.getTableHeader().setReorderingAllowed(false);
        cartTable.getTableHeader().setResizingAllowed(false);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.setRowHeight(28);
        cartTable.setBackground(Beige);
        cartTable.setForeground(DARK_BROWN);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        cartTable.getTableHeader().setBackground(BROWN);
        cartTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        scrollPane.getViewport().setBackground(Beige);
        this.add(scrollPane, BorderLayout.CENTER);

        // ── bottom panel ──────────────────────────────────────────────────────
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Beige);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 20, 30));

        // toggle-deselect on second click (unchanged)
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            private int lastRow = -1;
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = cartTable.rowAtPoint(e.getPoint());
                if (row == lastRow) { cartTable.clearSelection(); lastRow = -1; }
                else                { lastRow = row; }
            }
        });

        // [CHANGED] totalLabel moved from WEST to the same WEST position but
        // the old layout had:  WEST=totalLabel | EAST=btnPanel
        // The new layout keeps WEST=totalLabel | EAST=btnPanel unchanged,
        // but Back to Shop is now INSIDE btnPanel (see below) instead of having
        // its own separate westPanel wrapper — so the total stays on the left
        // and all buttons are grouped on the right.
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        totalLabel.setForeground(DARK_BROWN);

        // ── button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Beige);

        removeBtn   = StyleButtons("Remove Selected");

        // [ADDED] backBtn — was missing entirely in the original CartPanel.
        // It is placed between "Remove Selected" and "Clear Cart" so the
        // left-to-right order reads: Remove | ← Back to Shop | Clear Cart | Checkout
        // The ← arrow (\u2190) is a Unicode left-arrow so no font changes are needed.
        backBtn     = StyleButtons("\u2190 Back to Shop");

        clearBtn    = StyleButtons("Clear Cart");
        checkoutBtn = StyleButtons("Proceed to Checkout");

        btnPanel.add(removeBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(checkoutBtn);
        btnPanel.add(backBtn);  // [ADDED] sits right after Remove, before Clear

        // [UNCHANGED] total on left, buttons on right
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel,   BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);

        ButtonActions();
        refreshTable();
    }

    // ── style helpers (unchanged) ─────────────────────────────────────────────
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

    private void addHoverEffect(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(normal); }
        });
    }

    // ── table refresh (unchanged) ─────────────────────────────────────────────
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<CartItem> items = cartController.getCartItems();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            tableModel.addRow(new String[]{
                item.getBook().getTitle(),
                item.getBook().getAuthor(),
                String.valueOf(item.getQuantity()),
                String.format("$%.2f", item.getSubtotal())
            });
        }
        totalLabel.setText("Total: " + String.format("$%.2f", cartController.getTotal()));
    }

    // ── button actions ────────────────────────────────────────────────────────
    private void ButtonActions() {

        // [UNCHANGED] remove selected row
        removeBtn.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove the selected item(s)?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int bookId = cartController.getCartItems().get(row).getBook().getId();
                cartController.removeFromCart(bookId);
                refreshTable();
            }
        });

        // [FIX - CART BUG] was: new CatalogPanel(SessionManager.currentUser)
        // That created a brand-new CartController inside CatalogPanel, wiping all cart items.
        // Now we pass the existing cartController so the cart survives navigating back.
        backBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            CatalogPanel catalogPanel = new CatalogPanel(SessionManager.currentUser, cartController);
            parentFrame.setContentPane(catalogPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        // [UNCHANGED] clear all cart items
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear entire cart?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cartController.clearCart();
                refreshTable();
            }
        });

        // [UNCHANGED] navigate to checkout
        checkoutBtn.addActionListener(e -> {
            if (cartController.getCartItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            CheckOutPanel checkoutPanel = new CheckOutPanel(cartController);
            parentFrame.setContentPane(checkoutPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    }
}