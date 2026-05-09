package com.bookstore.view;

import com.bookstore.database.BookDAO;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;  

public class AdminPanel extends JPanel {

    private final User adminUser;
    private final BookDAO bookDAO = new BookDAO();
    private final DefaultTableModel tableModel;
    private final JTable booksTable;
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField genreField;
    private final JTextField priceField;
    private final JTextField stockField;
    private final JTextField coverField;

    public AdminPanel(User adminUser, JFrame parentFrame) {
        this.adminUser = adminUser;
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 240, 232));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel(
                "Admin Panel - " + (adminUser == null ? "Unknown User" : adminUser.getName()),
                SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
            "ID", "Title", "Author", "Genre", "Price", "Stock", "Cover"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(tableModel);
        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        formPanel.setBackground(new Color(245, 240, 232));
        titleField = createField("Title", 10);
        authorField = createField("Author", 10);
        genreField = createField("Genre", 8);
        priceField = createField("Price", 6);
        stockField = createField("Stock", 6);
        coverField = createField("Cover", 12);

        formPanel.add(titleField);
        formPanel.add(authorField);
        formPanel.add(genreField);
        formPanel.add(priceField);
        formPanel.add(stockField);
        formPanel.add(coverField);

        JButton addBookButton = new JButton("Add Book");
        JButton deleteBookButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");

        addBookButton.addActionListener(e -> addBook());
        deleteBookButton.addActionListener(e -> deleteSelectedBook());
        refreshButton.addActionListener(e -> loadBooks());

        formPanel.add(addBookButton);
        formPanel.add(deleteBookButton);
        formPanel.add(refreshButton);

        JButton backButton = new JButton("← Back to Shop");
        backButton.addActionListener(e -> {
            CatalogPanel catalogPanel = new CatalogPanel(adminUser);
            parentFrame.setContentPane(catalogPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        formPanel.add(backButton);

        add(formPanel, BorderLayout.SOUTH);
        loadBooks();
    }

    private JTextField createField(String tooltip, int columns) {
        JTextField field = new JTextField(columns);
        field.setToolTipText(tooltip);
        return field;
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = new ArrayList<>();
        try {
            books = bookDAO.getAllBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load books: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPrice(),
                book.getStock(),
                book.getCover()
            });
        }
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = genreField.getText().trim();
        String cover = coverField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty()
                || priceField.getText().trim().isEmpty() || stockField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            Book newBook = new Book(0, title, author, genre, price, stock, cover);
            bookDAO.addBook(newBook);
            clearForm();
            loadBooks();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and stock must be valid numbers.");
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a book to delete.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        bookDAO.deleteBook(id);
        loadBooks();
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
