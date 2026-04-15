package com.bookstore.view;

import javax.swing.*;
import java.awt.*;

public class CatalogPanel extends JPanel {

    public CatalogPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 240, 232));

        //NAVBAR
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 100); //navbar height
            }
        };

        navbar.setBackground(Color.WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 32, 20, 32));

        // LOGO
        JLabel logo = new JLabel("BOOKISH 🌸");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 25));
        logo.setForeground(new Color(59, 31, 10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/com/bookstore/view/ICON.png"));
        Image img = icon.getImage().getScaledInstance(90, 75, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(img));
        logo.setIconTextGap(0);

        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navLeft.setBackground(Color.WHITE);
        navLeft.add(logo);

        //NAV LINKS(RIGHT)
        JLabel homeLink = new JLabel("Home");
        homeLink.setFont(new Font("Arial", Font.PLAIN, 16));
        homeLink.setForeground(new Color(85, 85, 85));
        homeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //THE HOVERNG EFFECT
        homeLink.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
        homeLink.setForeground(new Color(139, 69, 19));
        }
         public void mouseExited(java.awt.event.MouseEvent e) {
        homeLink.setForeground(new Color(85, 85, 85));
        }
        });

        JLabel catalogLink = new JLabel("Catalog");
        catalogLink.setFont(new Font("Arial", Font.PLAIN, 16));
        catalogLink.setForeground(new Color(85, 85, 85));
        catalogLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        catalogLink.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
        catalogLink.setForeground(new Color(139, 69, 19));
         }
         public void mouseExited(java.awt.event.MouseEvent e) {
        catalogLink.setForeground(new Color(85, 85, 85));
         }
        });

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(139, 69, 19));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
          //hovering
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        registerBtn.setBackground(new Color(160, 82, 45));
    }
         //og color
    public void mouseExited(java.awt.event.MouseEvent evt) {
        registerBtn.setBackground(new Color(139, 69, 19));
    }

});

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        navRight.setBackground(Color.WHITE);
        navRight.add(homeLink);
        navRight.add(catalogLink);
        navRight.add(registerBtn);

        //ADD TO NAVBAR
        navbar.add(navLeft, BorderLayout.WEST);
        navbar.add(navRight, BorderLayout.EAST);

        //ADD NAVBAR TO PANEL
        add(navbar, BorderLayout.NORTH);
    }
}