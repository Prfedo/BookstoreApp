package com.bookstore.view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Panels (empty for now - teammates will fill them)
    private JPanel catalogPanel;
    private JPanel cartPanel;
    private JPanel ordersPanel;
    private JPanel loginPanel;

    public MainWindow() {
        setTitle("📚 Bookstore App");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        buildNavBar();
        buildPanels();

        setVisible(true);
    }

    // ── Navigation Bar ────────────────────────────────────
    private void buildNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(26, 35, 126)); 
        navBar.setPreferredSize(new Dimension(1000, 60));

        // App title on the left
        JLabel titleLabel = new JLabel(" Bookstore App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        navBar.add(titleLabel, BorderLayout.WEST);
        ImageIcon image = new ImageIcon("bo.jpg");
        

        // Navigation buttons on the right
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        navButtons.setBackground(new Color(26, 35, 126));

        JButton catalogBtn = createNavButton(" Catalog");
        JButton cartBtn    = createNavButton(" Cart");
        JButton ordersBtn  = createNavButton(" Orders");
        JButton loginBtn   = createNavButton(" Login");

        navButtons.add(catalogBtn);
        navButtons.add(cartBtn);
        navButtons.add(ordersBtn);
        navButtons.add(loginBtn);

        navBar.add(navButtons, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // Button actions — switch panels
        catalogBtn.addActionListener(e -> switchPanel("CATALOG"));
        cartBtn.addActionListener(e -> switchPanel("CART"));
        ordersBtn.addActionListener(e -> switchPanel("ORDERS"));
        loginBtn.addActionListener(e -> switchPanel("LOGIN"));
    }

    // ── Panels ────────────────────────────────────────────
    private void buildPanels() {
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        catalogPanel = buildPlaceholder(" Catalog Panel",
                "Book list will appear here — Member 3",
                new Color(232, 234, 246));

        cartPanel    = buildPlaceholder(" Cart Panel",
                "Shopping cart will appear here — Member 4",
                new Color(232, 245, 233));

        ordersPanel  = buildPlaceholder(" Orders Panel",
                "Order history will appear here — Member 4",
                new Color(255, 243, 224));

        loginPanel   = buildPlaceholder(" Login Panel",
                "Login & Register will appear here — Member 5",
                new Color(252, 228, 236));

        mainPanel.add(catalogPanel, "CATALOG");
        mainPanel.add(cartPanel,    "CART");
        mainPanel.add(ordersPanel,  "ORDERS");
        mainPanel.add(loginPanel,   "LOGIN");

        add(mainPanel, BorderLayout.CENTER);

        // Status bar at the bottom
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(236, 239, 241));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel statusLabel = new JLabel("  Welcome to Bookstore App!");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(96, 125, 139));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        // Show catalog first by default
        switchPanel("CATALOG");
    }

    // ── Helper — builds a placeholder panel ──────────────
    private JPanel buildPlaceholder(String title, String subtitle, Color bgColor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 22));
        titleLbl.setForeground(new Color(26, 35, 126));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        subLbl.setForeground(new Color(120, 120, 120));
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel comingLbl = new JLabel(" Coming Soon");
        comingLbl.setFont(new Font("Arial", Font.BOLD, 13));
        comingLbl.setForeground(new Color(255, 111, 0));
        comingLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(subLbl);
        card.add(Box.createVerticalStrut(15));
        card.add(comingLbl);

        panel.add(card);
        return panel;
    }

    // ── Helper — creates styled nav button ───────────────
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(63, 81, 181));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(92, 107, 192));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(63, 81, 181));
            }
        });
        return btn;
    }

    // ── Switch between panels ─────────────────────────────
    public void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}