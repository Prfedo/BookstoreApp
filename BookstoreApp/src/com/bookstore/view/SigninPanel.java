package com.bookstore.view;

// [NEW FILE] SigninPanel.java
// Why it was created:
//   The original registration was handled by SigninFrame.java, which extended
//   JFrame and opened a separate popup window.  That caused two windows to be
//   open at once, which is what the user wanted to fix.
//
//   SigninPanel extends JPanel instead of JFrame.
//   CatalogPanel now swaps the main window's content pane to this panel,
//   so registration happens inside the same single window.
//
//   SigninFrame.java is left untouched in the project; it is simply no longer
//   called by CatalogPanel.

import com.bookstore.database.UserDAO;
import com.bookstore.model.User;
import com.bookstore.model.SessionManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SigninPanel extends JPanel {

    // [SAME colours as SigninFrame so the look is identical]
    static final Color BG     = new Color(109, 76, 65);
    static final Color ACCENT = new Color(62, 39, 35);
    static final Color TEXT   = new Color(30, 30, 40);
    static final Color SUBTLE = new Color(120, 120, 140);

    private final UserDAO userDAO;

    // [ADDED] parentFrame reference — needed so we can swap the content pane
    // after a successful registration or when the user clicks "Sign in" link.
    private final JFrame parentFrame;

    public SigninPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userDAO = new UserDAO();

        // [SAME] background colour and centred card layout as SigninFrame
        setBackground(BG);
        setLayout(new GridBagLayout()); // GridBagLayout centres the card
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 235), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));
        // fixed size so the card looks the same as the old SigninFrame
        card.setMaximumSize(new Dimension(380, 600));
        card.setPreferredSize(new Dimension(380, 600));

        // ── form fields (same as SigninFrame) ────────────────────────────────
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JTextField nameField     = styledField();
        JTextField usernameField = styledField();
        JTextField emailField    = styledField();
        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField);

        // ── Create Account button ────────────────────────────────────────────
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
            public void mouseEntered(MouseEvent e) { signUpBtn.setBackground(new Color(109, 76, 65)); }
            public void mouseExited(MouseEvent e)  { signUpBtn.setBackground(ACCENT); }
        });
        signUpBtn.addActionListener(e -> {
            // [SAME validation as SigninFrame]
            String name = nameField.getText().trim();
            String u    = usernameField.getText().trim();
            String em   = emailField.getText().trim();
            String p    = new String(passwordField.getPassword());

            if (name.isEmpty() || u.isEmpty() || em.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Please fill in all fields.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!em.contains("@") || !em.contains(".")) {
                JOptionPane.showMessageDialog(parentFrame, "Please enter a valid email.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.length() < 6) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Password must be at least 6 characters.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean created = userDAO.register(name, u, em, p);
            if (created) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Account created! Welcome, " + name + "!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // [CHANGED vs SigninFrame] SigninFrame opened a new MainWindow(user).
                // Now: set the session and swap the content pane to a logged-in
                // CatalogPanel — same window, no popup.
                User user = userDAO.login(u, p);
                SessionManager.currentUser = user;
                CatalogPanel catalogPanel = new CatalogPanel(user);
                parentFrame.setContentPane(catalogPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Could not create account. Email may already exist.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ── "Already have an account?" footer link ───────────────────────────
        JLabel footer = new JLabel("Already have an account? Sign in");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(ACCENT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // [CHANGED vs SigninFrame] SigninFrame called new LoginFrame() (popup).
                // Now: swap to LoginPanel in the same window.
                LoginPanel loginPanel = new LoginPanel(parentFrame);
                parentFrame.setContentPane(loginPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });

        // ── assemble card (same order as SigninFrame) ─────────────────────────
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

    // ── helpers (same style as SigninFrame) ──────────────────────────────────
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