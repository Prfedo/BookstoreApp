package com.bookstore.view;

import com.bookstore.database.UserDAO;
import com.bookstore.model.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    static final Color BG = new Color(109, 76, 65);
    static final Color ACCENT = new Color(62, 39, 35);
    static final Color TEXT = new Color(30, 30, 40);
    static final Color SUBTLE = new Color(120, 120, 140);
    private final UserDAO userDAO;

        public LoginFrame() {
        this(null);
    }
    
    public LoginFrame(Window owner) {
        userDAO = new UserDAO();
        setTitle("Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(380, 460);
        setLocationRelativeTo(owner);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new GridBagLayout());
        add(buildCard());
        setVisible(true);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 235), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        // Title
        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(SUBTLE);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        // Fields
        JTextField usernameField = styledField();
        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField);

        // Button
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setBackground(ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(new Color(109, 76, 65));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(ACCENT);
            }
        });
        loginBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username/email and password.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = userDAO.login(u, p);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + user.getName() + "!");
                MainWindow window = new MainWindow();
                window.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Footer
        JLabel footer = new JLabel("Don't have an account? Sign up");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(ACCENT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SigninFrame();
                dispose();
            }
        });

        // Assemble
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(30));
        card.add(makeLabel("Username or Email"));
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(18));
        card.add(makeLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(28));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(footer);

        return card;
    }

    private JLabel makeLabel(String m) {
        JLabel L = new JLabel(m);
        L.setFont(new Font("SansSerif", Font.BOLD, 12));
        L.setForeground(SUBTLE);
        L.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        L.setAlignmentX(LEFT_ALIGNMENT);
        return L;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(TEXT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(TEXT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        f.setAlignmentX(LEFT_ALIGNMENT);
    }

}
