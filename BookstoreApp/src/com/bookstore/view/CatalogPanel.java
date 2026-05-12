package com.bookstore.view;

import com.bookstore.model.SessionManager;
import com.bookstore.controller.CartController;
import com.bookstore.database.BookDAO;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogPanel extends JPanel {

    private JScrollPane scrollPane;
    private JPanel booksGrid;
    private JTextField searchField;
    private JPanel genrePanel;
    private final BookDAO bookDAO = new BookDAO();
    private List<Book> allBooks = new ArrayList<>();
    private final java.util.Map<String, ImageIcon> imageCache = new java.util.HashMap<>(); // fix the lagging
    private CartController cartController;

    //hovercode for the button
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
    //hovercode for the label
    private void addHoverEffectt(JLabel btn, Color normal, Color hover) {

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(normal);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(hover);
            }
        });
    }

    public CatalogPanel() {
        this(null, new CartController());
    }

    public CatalogPanel(User user) {
        this(user, new CartController());
    }

    public CatalogPanel(User user, CartController cartController) {
        this.cartController = cartController;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 240, 232));

        //NAVBAR
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 100);
            }
        };

        navbar.setBackground(Color.WHITE);

        navbar.setBorder(BorderFactory.createEmptyBorder(15, 32, 20, 32));

        // LOGO
        JLabel logo = new JLabel("BOOKISH 🌸");
        logo.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 30));
        logo.setForeground(new Color(59, 31, 10));

        ImageIcon icon = new ImageIcon("pics/ICON.png");
        Image img = icon.getImage().getScaledInstance(85, 75, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(img));
        logo.setIconTextGap(0);
        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navLeft.setBackground(Color.WHITE);
        navLeft.add(logo);

     
        JLabel homeLink = new JLabel("Home");
        homeLink.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        homeLink.setForeground(new Color(85, 85, 85));
        homeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffectt(homeLink, new Color(139, 69, 19), new Color(85, 85, 85));
        homeLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
        

       
        JLabel cartLink = new JLabel("🛒 Cart");
        cartLink.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        cartLink.setForeground(new Color(85, 85, 85));
        cartLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffectt(cartLink, new Color(139, 69, 19), new Color(85, 85, 85));
        cartLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                CartPanel cartPanel = new CartPanel(cartController);
                parentFrame.setContentPane(cartPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
        
        

        JLabel catalogLink = new JLabel("Collection");
        catalogLink.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        catalogLink.setForeground(new Color(85, 85, 85));
        catalogLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffectt(catalogLink, new Color(139, 69, 19), new Color(85, 85, 85));
        catalogLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                booksGrid.scrollRectToVisible(booksGrid.getBounds());
                scrollPane.getVerticalScrollBar().setValue(booksGrid.getY());
            }
        });

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        navRight.setBackground(Color.WHITE);

        navRight.add(homeLink);
        navRight.add(catalogLink);
        navRight.add(cartLink);

        if (user != null) {
            JLabel userLabel = new JLabel("👤 " + user.getName());
            userLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            userLabel.setForeground(new Color(139, 69, 19));
            userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addHoverEffectt(userLabel, new Color(59, 31, 10), new Color(139, 69, 19));
            userLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                    // [FIXED] replaced JOptionPane stub with real OrderHistoryPanel navigation
                    OrderHistoryPanel historyPanel = new OrderHistoryPanel(cartController);
                    parentFrame.setContentPane(historyPanel);
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }
            });
            
            JButton logoutBtn = new JButton("Logout");
            logoutBtn.setBackground(new Color(139, 69, 19));
            logoutBtn.setForeground(Color.WHITE);
            logoutBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            logoutBtn.setBorderPainted(false);
            logoutBtn.setFocusPainted(false);
            logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addHoverEffect(logoutBtn, new Color(139, 69, 19), new Color(160, 82, 45));
            logoutBtn.addActionListener(e -> {
                SessionManager.currentUser = null; // clear global session
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                CatalogPanel guestPanel = new CatalogPanel(null); // rebuild as guest
                parentFrame.setContentPane(guestPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            });
            if (user.isAdmin()) {
                JButton adminBtn = new JButton("Admin Panel");
                adminBtn.setBackground(new Color(139, 69, 19));
                adminBtn.setForeground(Color.WHITE);
                adminBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                adminBtn.setBorderPainted(false);
                adminBtn.setFocusPainted(false);
                adminBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addHoverEffect(adminBtn, new Color(139, 69, 19), new Color(160, 82, 45));
                adminBtn.addActionListener(e -> {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                    AdminPanel adminPanel = new AdminPanel(user, parentFrame);
                    parentFrame.setContentPane(adminPanel);
                    parentFrame.revalidate();
                    parentFrame.repaint();
                });
                navRight.add(adminBtn);
            }

            navRight.add(userLabel);
            navRight.add(logoutBtn); 
        } else {
            
            JButton registerBtn = new JButton("Register");
            registerBtn.setBackground(new Color(139, 69, 19));
            registerBtn.setForeground(Color.WHITE);
            registerBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            registerBtn.setBorderPainted(false);
            registerBtn.setFocusPainted(false);
            registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addHoverEffect(registerBtn, new Color(139, 69, 19), new Color(160, 82, 45));
            registerBtn.addActionListener(e -> {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                SigninPanel signinPanel = new SigninPanel(parentFrame);
                parentFrame.setContentPane(signinPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            });

            JLabel loginLink = new JLabel("Login");
            loginLink.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            loginLink.setForeground(new Color(85, 85, 85));
            loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addHoverEffectt(loginLink, new Color(139, 69, 19), new Color(85, 85, 85));
            loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                    LoginPanel loginPanel = new LoginPanel(parentFrame);
                    parentFrame.setContentPane(loginPanel);
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }
            });

            navRight.add(loginLink);
            navRight.add(registerBtn);
        }

        //ADD NAVBAR
        navbar.add(navLeft, BorderLayout.WEST);
        navbar.add(navRight, BorderLayout.EAST);
        //ADD NAVBAR TO PANEL
        add(navbar, BorderLayout.NORTH);

        //the hero panel(its called like ts)
        JPanel heroPanel = new JPanel(null);
        heroPanel.setBackground(new Color(245, 240, 232)); // coloring the background
        heroPanel.setPreferredSize(new Dimension(1000, 270)); //sizw

        //quote
        JLabel qu = new JLabel("Books You'll Actually Finish");
        qu.setFont(new Font("Serif", Font.BOLD, 36));
        qu.setForeground(new Color(59, 31, 10));
        qu.setBounds(60, 25, 800, 50); 

        //discrption
        JLabel desc = new JLabel(
                "<html>"
                + "From learning to legends — explore a world of educational reads, page-turning thrillers,<br>"
                + "uplifting self-help, and magical fantasy adventures,<br>"
                + "where every page sparks curiosity, growth, and imagination."
                + "</html>"
        );
        desc.setFont(new Font("Georgia", Font.BOLD, 22));
        desc.setForeground(new Color(100, 100, 100));
        desc.setBounds(60, 60, 950, 150);

        //to collection button
        JButton BB = new JButton("To Your New TBR");
        BB.setBackground(new Color(139, 69, 19));
        BB.setForeground(Color.WHITE);
        BB.setFocusPainted(false);
        BB.setBorderPainted(false);
        BB.setFont(new Font("Georgia", Font.BOLD, 14));
        BB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        BB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                scrollPane.getVerticalScrollBar().setValue(booksGrid.getY());
            }
        });
        addHoverEffect(BB, new Color(160, 82, 45), new Color(139, 69, 19));
        BB.setBounds(60, 215, 200, 40); // start LOWEST

        // ADD TO PANEL
        heroPanel.add(qu);
        heroPanel.add(desc);
        heroPanel.add(BB);

       
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 240, 232));

        //ADD HERO
        centerPanel.add(heroPanel);

        //SEARCH BAR
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 10));
        searchPanel.setBackground(new Color(245, 240, 232));
        searchField = new JTextField(30) {
            private final String placeholder = "Search for books...";

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left, getHeight() - ins.bottom - g2.getFontMetrics().getDescent());
                    g2.dispose();
                }
            }
        };
        searchField.setFont(new Font("Georgia", Font.PLAIN, 14));
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
        // repaint on focus so placeholder appears/disappears cleanly
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                searchField.repaint();
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                searchField.repaint();
            }
        });
        centerPanel.add(searchPanel);

        // GENRE BUTTONS
        genrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        genrePanel.setBackground(new Color(245, 240, 232));
        genrePanel.setBorder(BorderFactory.createEmptyBorder(0, 52, 0, 52));

        String[] genres = {"All", "Fantasy", "Self-Help", "Thriller", "Biography", "Science Fiction", "Fiction", "Tech", "Finance"};

        for (String genre : genres) {
            JButton genreBtn = new JButton(genre) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (!getBackground().equals(new Color(139, 69, 19))) {
                        g2.setColor(new Color(180, 155, 130));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                    }
                    g2.dispose();
                }
            };
            genreBtn.setOpaque(false);
            genreBtn.setContentAreaFilled(false);
            genreBtn.setFont(new Font("Arial", Font.PLAIN, 13));
            genreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            genreBtn.setFocusPainted(false);
            genreBtn.setBorderPainted(false);
            genreBtn.setMargin(new Insets(6, 16, 6, 16));

            if (genre.equals("All")) {
                genreBtn.setBackground(new Color(139, 69, 19));
                genreBtn.setForeground(Color.WHITE);
            } else {
                genreBtn.setBackground(new Color(245, 240, 232));
                genreBtn.setForeground(new Color(80, 50, 20));
            }

            genreBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!genreBtn.getBackground().equals(new Color(139, 69, 19))) {
                        genreBtn.setBackground(new Color(230, 218, 200));
                    }
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!genreBtn.getBackground().equals(new Color(139, 69, 19))) {
                        genreBtn.setBackground(new Color(245, 240, 232));
                    }
                }
            });

            genreBtn.addActionListener(e -> {
                currentGenre = genre;
                filterBooks(searchField.getText());
                for (Component c : genrePanel.getComponents()) {
                    if (c instanceof JButton) {
                        JButton b = (JButton) c;
                        if (b.getText().equals(genre)) {
                            b.setBackground(new Color(139, 69, 19));
                            b.setForeground(Color.WHITE);
                        } else {
                            b.setBackground(new Color(245, 240, 232));
                            b.setForeground(new Color(80, 50, 20));
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

        loadBooks();
        renderBooks(allBooks);

        centerPanel.add(booksGrid);

        // MAKE IT SCROLLABLE
        scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setUnitIncrement(16);
        vBar.setPreferredSize(new Dimension(6, 0));
        vBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(139, 69, 19);
                trackColor = new Color(245, 240, 232);
            }

            @Override
            protected JButton createDecreaseButton(int o) {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int o) {
                return zeroButton();
            }

            private JButton zeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 6, 6);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(trackColor);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
        add(scrollPane, BorderLayout.CENTER);

    }
    private String currentGenre = "All";

    private void filterBooks(String searchText) {
        String normalizedSearch = searchText == null ? "" : searchText.trim();
        List<Book> source;
        if (normalizedSearch.isEmpty()) {
            source = allBooks;
        } else {
            source = searchBooksWithFallback(normalizedSearch);
        }
        List<Book> filtered = new ArrayList<>();
        for (Book book : source) {
            boolean matchesGenre = currentGenre.equals("All") || book.getGenre().equalsIgnoreCase(currentGenre);
            if (matchesGenre) {
                filtered.add(book);
            }
        }
        renderBooks(filtered);
    }

    private void loadBooks() {
        try {
            allBooks = bookDAO.getAllBooks();
        } catch (SQLException e) {
            allBooks = new ArrayList<>();
        }
    }

    private List<Book> searchBooksWithFallback(String title) {
        try {
            return bookDAO.searchBooks(title);
        } catch (Exception ignored) {
            List<Book> fallback = new ArrayList<>();
            for (Book book : allBooks) {
                if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    fallback.add(book);
                }
            }
            return fallback;
        }
    }

    private void renderBooks(List<Book> books) {
        booksGrid.removeAll();
        for (Book book : books) {
            booksGrid.add(createBookCard(book));
        }
        booksGrid.revalidate();
        booksGrid.repaint();
    }

    private JPanel createBookCard(Book book) {
        JPanel card, coverPanel, infoPanel;
        JLabel coverLabel, titleLabel, authorLabel;

        card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        coverPanel = new JPanel(new BorderLayout());
        coverPanel.setPreferredSize(new Dimension(350, 500));

        ImageIcon cachedIcon = imageCache.computeIfAbsent(book.getCover(), key -> {
            String normalized = key == null ? "" : key.replace("\\", "/");
            String fileName = normalized.substring(normalized.lastIndexOf('/') + 1);
            ImageIcon fileIcon = new ImageIcon("pics/books_cover/" + fileName);
            if (fileIcon.getIconWidth() <= 0) {
                return new ImageIcon(new BufferedImage(350, 500, BufferedImage.TYPE_INT_RGB));
            }
            Image scaled = fileIcon.getImage().getScaledInstance(350, 500, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        });

        coverLabel = new JLabel(cachedIcon);
        coverPanel.add(coverLabel, BorderLayout.CENTER);

        titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 13));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);   // CENTER

        authorLabel = new JLabel(book.getAuthor());
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

      
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel priceLabel = new JLabel(String.format("$%.2f", book.getPrice()));
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

        addbtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
            BookDetailDialog dialog = new BookDetailDialog(parent, book, cartController);
            dialog.setVisible(true);
        });

        footerPanel.add(priceLabel, BorderLayout.WEST);
        footerPanel.add(addbtn, BorderLayout.EAST);

        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(footerPanel);

        card.add(coverPanel);
        card.add(infoPanel);
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(CatalogPanel.this);
                BookDetailDialog dialog = new BookDetailDialog(parent, book, cartController);
                dialog.setVisible(true);
            }
        });
        return card;
    }
}
