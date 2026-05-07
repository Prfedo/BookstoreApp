package com.bookstore.view;

import com.bookstore.database.UserDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SigninFrame extends JFrame {

    static final Color BG     = new Color(109, 76, 65);
    static final Color ACCENT = new Color(62, 39, 35);
    static final Color TEXT   = new Color(30, 30, 40);
    static final Color SUBTLE = new Color(120, 120, 140);
    private final UserDAO userDAO;

    public SigninFrame() {
        this(null);
    }

    public SigninFrame(Window owner) {
        userDAO = new UserDAO();
        setTitle("Sign Up");
        setDefaultCloseOperation(owner != null ? DISPOSE_ON_CLOSE : EXIT_ON_CLOSE);
        setSize(380, 600);
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
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));


        // Fields
        JTextField nameField = styledField();
        JTextField usernameField = styledField();
        JTextField emailField    = styledField();
        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField);

        // Button
        JButton signUpBtn = new JButton("Create Account");
        signUpBtn.setAlignmentX(LEFT_ALIGNMENT);
        signUpBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        signUpBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpBtn.setBackground(ACCENT);
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setBorderPainted(false);
        signUpBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                signUpBtn.setBackground(new Color(109, 76, 65));
            }
            @Override public void mouseExited(MouseEvent e) {
                signUpBtn.setBackground(ACCENT);
            }
        });
        signUpBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String u = usernameField.getText().trim();
            String em = emailField.getText().trim();
            String p = new String(passwordField.getPassword());

            if (name.isEmpty() || u.isEmpty() || em.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!em.contains("@") || !em.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean created = userDAO.register(name, u, em, p);
            if (created) {
                JOptionPane.showMessageDialog(this, "Account created! Please sign in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not create account. Email may already exist.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Footer
        JLabel footer = new JLabel("Already have an account? Sign in");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(ACCENT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame();
                dispose();
            }
        });

        // Assemble
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        card.add(makeLabel("Name"));
        card.add(Box.createVerticalStrut(6));
        card.add(nameField);
        card.add(Box.createVerticalStrut(15));
        card.add(makeLabel("Username"));
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(makeLabel("Email Address"));
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(makeLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(28));
        card.add(signUpBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(footer);

        return card;
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(SUBTLE);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(TEXT);
        f.setAlignmentX(LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(TEXT);
        f.setAlignmentX(LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

}