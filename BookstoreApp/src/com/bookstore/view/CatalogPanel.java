package com.bookstore.view;

import javax.swing.*;
import java.awt.*;

public class CatalogPanel extends JPanel {

    private JPanel booksGrid;
    private JTextField searchField;
    private JPanel genrePanel;
    private final java.util.Map<String, ImageIcon> imageCache = new java.util.HashMap<>(); // FIX 2: cache

    //hovercode
    private void addHoverEffect(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normal);
            }
        });
    }

    public String[][] books = {
            {"Fourth Wing: Empyrean", "Rebecca Yarros", "Fantasy", "$13.88","FW.jpg"},
            {"The Cruel Prince", " Holly Black", "Fantasy", "$21.99","CP.jpg"},
            {"The Silent Patient", "Alex Michaelides", "Thriller", "$16.99","SP.jpg"},
            {"Throne of Glass", "Sarah J. Maas", "Fantasy", "$29.99","TOG.jpg"},
            {"Project Hail Mary", "Andy Weir", "Science Fiction", "$24.99","PHM.jpg"},
            {"Song of Achilles", "Madlin Miller", "Fiction", "$19.99","SOA.jpg"},
            {"The Alchemist", "Paulo Coelho", "Fiction", "$14.99","TA.jpg"},
            {"Dune", "Frank Herbert", "Science Fiction", "$22.99","D.jpg"}
        };
    public CatalogPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 240, 232));

        //NAVBAR
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 100);//navbar height
            }
        };

        navbar.setBackground(Color.WHITE);

        navbar.setBorder(BorderFactory.createEmptyBorder(15, 32, 20, 32));

        // LOGO
        JLabel logo = new JLabel("BOOKISH 🌸");

        logo.setFont(
                new Font("Segoe UI Emoji", Font.BOLD,30));
        logo.setForeground(
                new Color(59, 31, 10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/com/bookstore/view/ICON.png"));
        Image img = icon.getImage().getScaledInstance(85, 75, Image.SCALE_SMOOTH);

        logo.setIcon(
                new ImageIcon(img));
        logo.setIconTextGap(0);

        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navLeft.setBackground(Color.WHITE);
        navLeft.add(logo);

        //NAV LINKS(RIGHT)
        JLabel homeLink = new JLabel("Home");
        homeLink.setFont(
                new Font("Arial", Font.PLAIN, 16));
        homeLink.setForeground(
                new Color(85, 85, 85));
        homeLink.setCursor(
                new Cursor(Cursor.HAND_CURSOR));
        //THE HOVERNG EFFECT
        homeLink.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent e) {
                homeLink.setForeground(new Color(139, 69, 19));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                homeLink.setForeground(new Color(85, 85, 85));
            }
        }
        );

        JLabel catalogLink = new JLabel("Collection");

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
        addHoverEffect(registerBtn, new Color(139, 69, 19), new Color(160, 82, 45));

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));

        navRight.setBackground(Color.WHITE);
        navRight.add(homeLink);
        navRight.add(catalogLink);
        navRight.add(registerBtn);

        //ADD NAVBAR
        navbar.add(navLeft, BorderLayout.WEST);
        navbar.add(navRight, BorderLayout.EAST);
        //ADD NAVBAR TO PANEL
        add(navbar, BorderLayout.NORTH);

        //the hero panel(its called like ts)
        JPanel heroPanel = new JPanel(null);//to animate the text
        heroPanel.setBackground(new Color(245, 240, 232)); // coloring the background
        heroPanel.setPreferredSize(new Dimension(1000, 250)); //sizw

        //quote
        JLabel qu = new JLabel("Books You'll Actually Finish");
        qu.setFont(new Font("Serif", Font.BOLD, 36));
        qu.setForeground(new Color(59, 31, 10));
        qu.setBounds(60, 25, 800, 50); // start LOW

        //discrption
        JLabel desc = new JLabel(
                "<html>From learning to legends — dive into educational books, page-turning thrillers,"
                + "<br>uplifting self-help, and immersive fantasy worlds.</html>"
        );
        desc.setFont(new Font("Arial", Font.PLAIN, 16));
        desc.setForeground(new Color(100, 100, 100));
        desc.setBounds(60, 90, 600, 50); // start LOWER

        //to collection button
        JButton BB = new JButton("To Your New TBR");

        BB.setBackground(new Color(139, 69, 19));
        BB.setForeground(Color.WHITE);
        BB.setFocusPainted(false);
        BB.setBorderPainted(false);
        BB.setFont(new Font("Arial", Font.BOLD, 14));
        BB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(BB, new Color(160, 82, 45), new Color(139, 69, 19));
        BB.setBounds(60, 150, 200, 40); // start LOWEST

        // ADD TO PANEL
        heroPanel.add(qu);
        heroPanel.add(desc);
        heroPanel.add(BB);

         //SEARCH + GENRES + BOOKS all in one scrollable center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 240, 232));

        //ADD HERO
        centerPanel.add(heroPanel);

       //SEARCH BAR
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 10));
        searchPanel.setBackground(new Color(245, 240, 232));
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        searchPanel.add(searchField);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = searchField.getText().toLowerCase();
                filterBooks(text);
            }
        });
        centerPanel.add(searchPanel);

// GENRE BUTTONS
        genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 5));
        genrePanel.setBackground(new Color(245, 240, 232));
        String[] genres = {"All", "Fantasy", "Self-Help", "Thriller", "Biography", "Science Fiction", "Fiction"};
        for (String genre : genres) {
    JButton genreBtn = new JButton(genre);
    genreBtn.setFont(new Font("Arial", Font.PLAIN, 13));
    genreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    genreBtn.setFocusPainted(false);

    if (genre.equals("All")) {
        genreBtn.setBackground(new Color(139, 69, 19));
        genreBtn.setForeground(Color.WHITE);
        genreBtn.setBorderPainted(false);
    } else {
        genreBtn.setBackground(Color.WHITE);
        genreBtn.setForeground(new Color(60, 60, 60));
        genreBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
    }

    genreBtn.addActionListener(e -> {
        currentGenre = genre;
        filterBooks(searchField.getText());

        // Reset all buttons style
        for (Component c : genrePanel.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                if (b.getText().equals(genre)) {
                    b.setBackground(new Color(139, 69, 19));
                    b.setForeground(Color.WHITE);
                    b.setBorderPainted(false);
                } else {
                    b.setBackground(Color.WHITE);
                    b.setForeground(new Color(60, 60, 60));
                    b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
                }
            }
        }
    });
    genrePanel.add(genreBtn);
}
        centerPanel.add(genrePanel);

        // BOOKS GRID
        booksGrid = new JPanel(new GridLayout(0, 4, 16, 16));
        booksGrid.setBackground(new Color(245, 240, 232));
        booksGrid.setBorder(BorderFactory.createEmptyBorder(16, 60, 32, 60));

        for (String[] book : books) {
            booksGrid.add(createBookCard(book)); // FIX: use createBookCard instead of duplicating
        }

        centerPanel.add(booksGrid);

        // MAKE IT SCROLLABLE
        JScrollPane scrollPane = new JScrollPane(centerPanel);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    private String currentGenre = "All";

   private void filterBooks(String searchText) {
    booksGrid.removeAll();
    for (String[] book : books) {
        boolean matchesSearch = book[0].toLowerCase().contains(searchText.toLowerCase());
        boolean matchesGenre = currentGenre.equals("All") || book[2].equals(currentGenre);
        if (matchesSearch && matchesGenre) {
            booksGrid.add(createBookCard(book));
        }
    }
    booksGrid.revalidate();
    booksGrid.repaint();
}

  private JPanel createBookCard(String[] book) {
    JPanel card ,coverPanel,infoPanel;
    JLabel coverLabel ,titleLabel,authorLabel;

    card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createLineBorder(new Color(100,100,100),1));

    // COVER — FIX 2: use cache to avoid re-scaling on every filter
    coverPanel = new JPanel(new BorderLayout());
    coverPanel.setPreferredSize(new Dimension(350, 500));

    ImageIcon cachedIcon = imageCache.computeIfAbsent(book[4], key -> {
        java.net.URL imgURL = getClass().getResource("/com/bookstore/view/I/" + key);
        Image scaled = new ImageIcon(imgURL).getImage().getScaledInstance(350, 500, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    });

    coverLabel = new JLabel(cachedIcon);
    coverPanel.add(coverLabel, BorderLayout.CENTER);

    titleLabel = new JLabel(book[0]);
    titleLabel.setFont(new Font("Georgia", Font.BOLD, 13));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);   // CENTER

    authorLabel = new JLabel(book[1]);
    authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // CENTER

    infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBackground(Color.WHITE);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));
    infoPanel.setPreferredSize(new Dimension(100, 85));

    infoPanel.add(Box.createVerticalGlue()); // pushes content DOWN

    infoPanel.add(titleLabel);
    infoPanel.add(Box.createVerticalStrut(3));
    infoPanel.add(authorLabel);

    // FIX 1: add the missing footer with price + Add to Cart
    JPanel footerPanel = new JPanel(new BorderLayout());
    footerPanel.setBackground(Color.WHITE);
    footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

    JLabel priceLabel = new JLabel(book[3]);
    priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
    priceLabel.setForeground(new Color(139, 69, 19));

    JButton addbtn = new JButton("Add To Cart");
    addbtn.setBackground(new Color(139, 69, 19));
    addbtn.setForeground(Color.WHITE);
    addbtn.setFont(new Font("Arial", Font.BOLD, 12));
    addbtn.setBorderPainted(false);
    addbtn.setFocusPainted(false);
    addbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    addHoverEffect(addbtn, new Color(139, 69, 19), new Color(160, 82, 45));

    footerPanel.add(priceLabel, BorderLayout.WEST);
    footerPanel.add(addbtn, BorderLayout.EAST);

    infoPanel.add(Box.createVerticalStrut(8));
    infoPanel.add(footerPanel);

    card.add(coverPanel);
    card.add(infoPanel);

    return card;
 }
}