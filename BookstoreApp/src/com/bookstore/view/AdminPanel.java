package com.bookstore.view;

import com.bookstore.database.BookDAO;
import com.bookstore.database.UserDAO;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class AdminPanel extends JPanel {

    private static final Color BEIGE      = new Color(245, 240, 232);
    private static final Color BROWN      = new Color(139, 69, 19);
    private static final Color BROWN_HOV  = new Color(160, 82, 45);
    private static final Color DARK_BROWN = new Color(59, 31, 10);
    private static final Color WHITE      = Color.WHITE;
    private static final Color CARD_BORDER = new Color(210, 195, 180);
    private static final Color DANGER     = new Color(180, 50, 50);
    private static final Color DANGER_HOV = new Color(200, 70, 70);

    private final User adminUser;
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final DefaultTableModel tableModel;
    private final JTable booksTable;

    
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField genreField;
    private final JTextField priceField;
    private final JTextField stockField;
    private final JTextField coverField;

   
    private final JTextField deleteUsernameField;

    public AdminPanel(User adminUser, JFrame parentFrame) {
        this.adminUser = adminUser;
        setLayout(new BorderLayout());
        setBackground(BEIGE);

        
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        JLabel logo = new JLabel("BOOKISH 🌸  ·  Admin Panel");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        logo.setForeground(DARK_BROWN);

        JLabel adminLabel = new JLabel("👤 " + (adminUser == null ? "Admin" : adminUser.getName()));
        adminLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        adminLabel.setForeground(BROWN);

        JButton backBtn = styledButton("← Back to Shop", BROWN, BROWN_HOV);
        backBtn.addActionListener(e -> {
            CatalogPanel catalogPanel = new CatalogPanel(adminUser);
            parentFrame.setContentPane(catalogPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        navRight.setBackground(WHITE);
        navRight.add(adminLabel);
        navRight.add(backBtn);

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(navRight, BorderLayout.EAST);
        add(navbar, BorderLayout.NORTH);

        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(BEIGE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Price", "Stock", "Cover"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        booksTable = new JTable(tableModel);
        booksTable.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        booksTable.setRowHeight(30);
        booksTable.setBackground(WHITE);
        booksTable.setForeground(DARK_BROWN);
        booksTable.setSelectionBackground(new Color(210, 180, 140));
        booksTable.setSelectionForeground(DARK_BROWN);
        booksTable.setGridColor(new Color(220, 210, 200));
        booksTable.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        booksTable.getTableHeader().setBackground(BROWN);
        booksTable.getTableHeader().setForeground(WHITE);
        booksTable.getTableHeader().setReorderingAllowed(false);
        booksTable.getTableHeader().setResizingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < booksTable.getColumnCount(); i++) {
            booksTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(50);
        booksTable.getColumnModel().getColumn(6).setPreferredWidth(140);

        JScrollPane tableScroll = new JScrollPane(booksTable);
        tableScroll.setBorder(null);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollBar(tableScroll.getVerticalScrollBar());

        JButton refreshBtn = styledButton("🔄 Refresh", BROWN, BROWN_HOV);
        JButton deleteBookBtn = styledButton("🗑️ Delete Book", BROWN, BROWN_HOV);
        JPanel tableActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        tableActions.setBackground(BEIGE);
        tableActions.add(deleteBookBtn);
        tableActions.add(refreshBtn);

        JLabel tableTitle = new JLabel("📚  Book Inventory");
        tableTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        tableTitle.setForeground(DARK_BROWN);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BEIGE);
        tableWrapper.add(tableTitle,   BorderLayout.NORTH);
        tableWrapper.add(tableScroll,  BorderLayout.CENTER);
        tableWrapper.add(tableActions, BorderLayout.SOUTH);

       
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BEIGE);
        sidebar.setPreferredSize(new Dimension(300, 0));

       
        deleteUsernameField = formField();

        JButton deleteUserBtn = new JButton("Delete User");
        deleteUserBtn.setBackground(DANGER);
        deleteUserBtn.setForeground(WHITE);
        deleteUserBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        deleteUserBtn.setBorderPainted(false);
        deleteUserBtn.setFocusPainted(false);
        deleteUserBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        deleteUserBtn.setAlignmentX(LEFT_ALIGNMENT);
        deleteUserBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { deleteUserBtn.setBackground(DANGER_HOV); }
            public void mouseExited(java.awt.event.MouseEvent e)  { deleteUserBtn.setBackground(DANGER); }
        });

        JPanel userCard = buildCard();
        userCard.add(sectionTitle("🗑️  Delete User"));
        userCard.add(Box.createVerticalStrut(4));
        userCard.add(divider());
        userCard.add(Box.createVerticalStrut(12));
        userCard.add(fieldLabel("Username"));
        userCard.add(Box.createVerticalStrut(4));
        deleteUsernameField.setAlignmentX(LEFT_ALIGNMENT);
        userCard.add(deleteUsernameField);
        userCard.add(Box.createVerticalStrut(14));
        userCard.add(deleteUserBtn);

        
        titleField  = formField();
        authorField = formField();
        genreField  = formField();
        priceField  = formField();
        stockField  = formField();
        coverField  = formField();
        addNumericFilter(priceField, true);
        addNumericFilter(stockField, false);

        JButton addBtn = styledButton("➕ Add Book", BROWN, BROWN_HOV);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        addBtn.setAlignmentX(LEFT_ALIGNMENT);

        JPanel bookCard = buildCard();
        bookCard.add(sectionTitle("➕  Add New Book"));
        bookCard.add(Box.createVerticalStrut(4));
        bookCard.add(divider());
        bookCard.add(Box.createVerticalStrut(12));
        addSimpleField(bookCard, "Title",        titleField);
        addSimpleField(bookCard, "Author",       authorField);
        addSimpleField(bookCard, "Genre",        genreField);
        addSimpleField(bookCard, "Price ($)",    priceField);
        addSimpleField(bookCard, "Stock",        stockField);
        addSimpleField(bookCard, "Cover file",   coverField);
        bookCard.add(Box.createVerticalStrut(6));
        bookCard.add(addBtn);

       
        sidebar.add(sidebarSection("Manage Users", userCard));
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sidebarSection("New Book", bookCard));
        sidebar.add(Box.createVerticalGlue());

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setBorder(null);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.getViewport().setBackground(BEIGE);
        styleScrollBar(sidebarScroll.getVerticalScrollBar());

        contentPanel.add(tableWrapper,  BorderLayout.CENTER);
        contentPanel.add(sidebarScroll, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        
        addBtn.addActionListener(e        -> addBook());
        deleteBookBtn.addActionListener(e -> deleteSelectedBook());
        refreshBtn.addActionListener(e    -> loadBooks());
        deleteUserBtn.addActionListener(e -> deleteUserByUsername());

        loadBooks();
    }

    
    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER, 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

   
    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        lbl.setForeground(DARK_BROWN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

   
    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(CARD_BORDER);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
    }

    
    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lbl.setForeground(DARK_BROWN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    
    private void addSimpleField(JPanel parent, String label, JTextField field) {
        parent.add(fieldLabel(label));
        parent.add(Box.createVerticalStrut(4));
        field.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
    }

    
    private JPanel sidebarSection(String headerText, JPanel card) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BEIGE);

        JLabel header = new JLabel(headerText);
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        header.setForeground(DARK_BROWN);
        header.setAlignmentX(LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 2, 8, 0));

        wrapper.add(header);
        wrapper.add(card);
        return wrapper;
    }

    private JTextField formField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        f.setForeground(DARK_BROWN);
        f.setBackground(WHITE);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 185, 170), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    private JButton styledButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 34));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void styleScrollBar(JScrollBar vBar) {
        vBar.setUnitIncrement(16);
        vBar.setPreferredSize(new Dimension(6, 0));
        vBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = BROWN;
                trackColor = BEIGE;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 6, 6);
                g2.dispose();
            }
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(trackColor);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
    }

    private void addNumericFilter(JTextField field, boolean allowDecimal) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    if (allowDecimal && c == '.' && !field.getText().contains(".")) return;
                    e.consume();
                }
            }
        });
    }

    
    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = new ArrayList<>();
        try {
            books = bookDAO.getAllBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load books: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPrice(), book.getStock(), book.getCover()
            });
        }
    }

    private void addBook() {
        String title  = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre  = genreField.getText().trim();
        String cover  = coverField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty()
                || priceField.getText().trim().isEmpty()
                || stockField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields before adding a book.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            int    stock = Integer.parseInt(stockField.getText().trim());
            bookDAO.addBook(new Book(0, title, author, genre, price, stock, cover));
            clearForm();
            loadBooks();
            JOptionPane.showMessageDialog(this,
                    "\"" + title + "\" added successfully!",
                    "Book Added", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Price and stock must be valid numbers.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String bookTitle = tableModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete \"" + bookTitle + "\" permanently?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            bookDAO.deleteBook(id);
            loadBooks();
        }
    }

    private void deleteUserByUsername() {
        String username = deleteUsernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a username.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (adminUser != null && adminUser.getUsername().equalsIgnoreCase(username)) {
            JOptionPane.showMessageDialog(this,
                    "You cannot delete your own admin account.",
                    "Not Allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Permanently delete user \"" + username + "\"?\nThis cannot be undone.",
                "Confirm Delete User", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = userDAO.deleteUser(username);
            if (deleted) {
                JOptionPane.showMessageDialog(this,
                        "User \"" + username + "\" has been deleted.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                deleteUsernameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No user found with username \"" + username + "\".",
                        "Not Found", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        genreField.setText("");
        priceField.setText("");
        stockField.setText("");
        coverField.setText("");
    }
}