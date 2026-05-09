package com.bookstore.view;

import com.bookstore.database.BookDAO;
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

    // ── palette (matches the rest of the app) ────────────────────────────────
    private static final Color BEIGE      = new Color(245, 240, 232);
    private static final Color BROWN      = new Color(139, 69, 19);
    private static final Color BROWN_HOV  = new Color(160, 82, 45);
    private static final Color DARK_BROWN = new Color(59, 31, 10);
    private static final Color WHITE      = Color.WHITE;

    private final User adminUser;
    private final BookDAO bookDAO = new BookDAO();
    private final DefaultTableModel tableModel;
    private final JTable booksTable;

    // form fields
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField genreField;
    private final JTextField priceField;
    private final JTextField stockField;
    private final JTextField coverField;

    public AdminPanel(User adminUser, JFrame parentFrame) {
        this.adminUser = adminUser;
        setLayout(new BorderLayout());
        setBackground(BEIGE);

        // ── TOP NAVBAR ────────────────────────────────────────────────────────
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        JLabel logo = new JLabel("BOOKISH 🌸  ·  Admin Panel");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        logo.setForeground(DARK_BROWN);

        JLabel adminLabel = new JLabel("👤 " + (adminUser == null ? "Admin" : adminUser.getName()));
        adminLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        adminLabel.setForeground(BROWN);

        JButton backBtn = styledButton("← Back to Shop");
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

        // ── MAIN CONTENT: table (left) + form (right) ─────────────────────────
        JPanel contentPanel = new JPanel(new BorderLayout(16, 0));
        contentPanel.setBackground(BEIGE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        // ── BOOK TABLE ────────────────────────────────────────────────────────
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

        // center-align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < booksTable.getColumnCount(); i++) {
            booksTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // column widths
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(35);   // ID
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(160);  // Title
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Author
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(90);   // Genre
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(60);   // Price
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(50);   // Stock
        booksTable.getColumnModel().getColumn(6).setPreferredWidth(140);  // Cover

        JScrollPane tableScroll = new JScrollPane(booksTable);
        tableScroll.setBorder(new LineBorder(new Color(210, 195, 180), 1));
        tableScroll.getViewport().setBackground(WHITE);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BEIGE);

        JLabel tableTitle = new JLabel("📚  Book Inventory");
        tableTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        tableTitle.setForeground(DARK_BROWN);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Refresh button below table
        JButton refreshBtn = styledButton("🔄 Refresh Table");
        JButton deleteBtn  = styledButton("🗑️  Delete Selected");
        JPanel tableActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        tableActions.setBackground(BEIGE);
        tableActions.add(deleteBtn);
        tableActions.add(refreshBtn);

        tableWrapper.add(tableTitle,   BorderLayout.NORTH);
        tableWrapper.add(tableScroll,  BorderLayout.CENTER);
        tableWrapper.add(tableActions, BorderLayout.SOUTH);

        // ── ADD BOOK FORM (right side) ────────────────────────────────────────
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(WHITE);
        formCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 195, 180), 1, true),
                new EmptyBorder(20, 22, 20, 22)
        ));
        formCard.setPreferredSize(new Dimension(290, 0));

        JLabel formTitle = new JLabel("Add New Book");
        formTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
        formTitle.setForeground(DARK_BROWN);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(210, 195, 180));

        titleField  = formField();
        authorField = formField();
        genreField  = formField();
        priceField  = formField();
        stockField  = formField();
        coverField  = formField();

        // restrict price/stock to numeric input
        addNumericFilter(priceField, true);   // decimals allowed
        addNumericFilter(stockField, false);  // integers only

        JButton addBtn = styledButton("➕ Add Book");
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        addBtn.setAlignmentX(LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(sep);
        formCard.add(Box.createVerticalStrut(16));

        addLabeledField(formCard, "Title",
                "The full name of the book as it appears on the cover.", titleField);
        addLabeledField(formCard, "Author",
                "First and last name of the author (e.g. J.K. Rowling).", authorField);
        addLabeledField(formCard, "Genre",
                "Category the book belongs to (e.g. Fantasy, Thriller, Self-Help).", genreField);
        addLabeledField(formCard, "Price ($)",
                "Selling price in US dollars. Use decimals for cents (e.g. 12.99).", priceField);
        addLabeledField(formCard, "Stock",
                "Number of copies currently available in inventory.", stockField);
        addLabeledField(formCard, "Cover filename",
                "Image filename inside pics/books_cover/ (e.g. atomic_habits.jpg).", coverField);

        formCard.add(Box.createVerticalStrut(18));
        formCard.add(addBtn);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BEIGE);
        JLabel formSection = new JLabel("➕  New Book");
        formSection.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        formSection.setForeground(DARK_BROWN);
        formSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        formWrapper.add(formSection, BorderLayout.NORTH);
        formWrapper.add(formCard,    BorderLayout.CENTER);

        contentPanel.add(tableWrapper, BorderLayout.CENTER);
        contentPanel.add(formWrapper,  BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        // ── wire up buttons ───────────────────────────────────────────────────
        addBtn.addActionListener(e    -> addBook());
        deleteBtn.addActionListener(e -> deleteSelectedBook());
        refreshBtn.addActionListener(e -> loadBooks());

        loadBooks();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /**
     * Adds a label, a small description line, and the field to the form panel.
     */
    private void addLabeledField(JPanel parent, String labelText,
                                  String description, JTextField field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lbl.setForeground(DARK_BROWN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>" + description + "</html>");
        desc.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        desc.setForeground(new Color(140, 120, 100));
        desc.setAlignmentX(LEFT_ALIGNMENT);

        field.setAlignmentX(LEFT_ALIGNMENT);

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(2));
        parent.add(desc);
        parent.add(Box.createVerticalStrut(4));
        parent.add(field);
        parent.add(Box.createVerticalStrut(12));
    }

    /** A styled text field matching the bookstore form design. */
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

    /** A styled brown button. */
    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BROWN);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 34));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(BROWN_HOV); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(BROWN); }
        });
        return btn;
    }

    /** Restricts a text field to numeric input; allowDecimal lets through one '.'. */
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

    // ── data operations (logic unchanged from original) ───────────────────────

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

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        genreField.setText("");
        priceField.setText("");
        stockField.setText("");
        coverField.setText("");
    }
}