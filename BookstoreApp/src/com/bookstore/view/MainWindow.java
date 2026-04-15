package com.bookstore.view;

import javax.swing.*;
import java.awt.Image;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("BOOKSTORE");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Appicon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/bookstore/view/Appicon.jpg"));
            Image img = icon.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
            setIconImage(img);
        } catch (Exception e) {
            System.out.println("Icon error: " + e.getMessage());
        }
        CatalogPanel catalogPanel = new CatalogPanel();
        add(catalogPanel);
    }
}