package com.bookstore.view;

// [NEW FILE] LoginPanel.java
// Why it was created:
//   The original login was done through LoginFrame.java, which extended JFrame
//   and opened a brand-new popup window every time the user clicked "Login".
//   That meant two windows were open at the same time (the main bookstore window
//   + the login popup), which is what the user wanted to fix.
//
//   LoginPanel extends JPanel instead of JFrame.
//   CatalogPanel now swaps the main window's content pane to this panel,
//   so login happens inside the same single window — no popup ever appears.
//
//   LoginFrame.java is left untouched in the project because removing it would
//   require changing other files that might reference it. It is simply no longer
//   called by CatalogPanel.

import com.bookstore.database.UserDAO;
import com.bookstore.model.User;
import com.bookstore.model.SessionManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {

    // [SAME colours as LoginFrame so the look is identical]
    static final Color BG     = new Color(109, 76, 65);
    static final Color ACCENT = new Color(62, 39, 35);
    static final Color TEXT   = new Color(30, 30, 40);
    static final Color SUBTLE = new Color(120, 120, 140);

    private final UserDAO userDAO;

    // [ADDED] parentFrame reference — needed so we can swap the content pane
    // after a successful login or when the user clicks "Sign up" link.
    private final JFrame parentFrame;

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userDAO = new UserDAO();

        // [SAME] background colour and centred card layout as LoginFrame
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
        // fixed size so the card looks the same as the old LoginFrame
        card.setMaximumSize(new Dimension(380, 460));
        card.setPreferredSize(new Dimension(380, 460));

        // ── form fields (same as LoginFrame) ────────────────────────────────
        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(SUBTLE);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        JTextField usernameField = styledField();
        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField);

        // ── Sign In button ───────────────────────────────────────────────────
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
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(new Color(109, 76, 65)); }
            public void mouseExited(MouseEvent e)  { loginBtn.setBackground(ACCENT); }
        });
        loginBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Please enter your username/email and password.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User user = userDAO.login(u, p);
            if (user != null) {
                // [CHANGED vs LoginFrame] LoginFrame opened a new MainWindow(user).
                // Now: set the session and swap the content pane to a logged-in
                // CatalogPanel — the window never changes, only its contents do.
                SessionManager.currentUser = user;
                CatalogPanel catalogPanel = new CatalogPanel(user);
                parentFrame.setContentPane(catalogPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Invalid username or password.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ── "Don't have an account?" footer link ─────────────────────────────
        JLabel footer = new JLabel("Don't have an account? Sign up");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(ACCENT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // [CHANGED vs LoginFrame] LoginFrame called new SigninFrame() (popup).
                // Now: swap to SigninPanel in the same window.
                SigninPanel signinPanel = new SigninPanel(parentFrame);
                parentFrame.setContentPane(signinPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });

        // ── assemble card (same order as LoginFrame) ─────────────────────────
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

    // ── helpers (same style as LoginFrame) ──────────────────────────────────
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