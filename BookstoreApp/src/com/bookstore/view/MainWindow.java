package com.bookstore.view;

import javax.swing.*;
import java.awt.Image;

public class MainWindow extends JFrame {

    public MainWindow() {
        //JFrame f = new JFrame();
        setTitle("BOOKSTORE");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //ImageIcon img = new ImageIcon("/com/bookstore/view/Appicon.png");
        //f.setIconImage(img.getImage());
        CatalogPanel catalogPanel = new CatalogPanel();
        add(catalogPanel);
    }
}
