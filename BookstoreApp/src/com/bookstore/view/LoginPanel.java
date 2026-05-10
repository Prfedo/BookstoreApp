package com.bookstore.view;


import com.bookstore.database.UserDAO;
import com.bookstore.model.User;
import com.bookstore.model.SessionManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {

    static final Color BG     = new Color(109, 76, 65);
    static final Color ACCENT = new Color(62, 39, 35);
    static final Color TEXT   = new Color(30, 30, 40);
    static final Color SUBTLE = new Color(120, 120, 140);

    private final UserDAO userDAO;

    private final JFrame parentFrame;

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userDAO = new UserDAO();

        setBackground(BG);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topBar.setBackground(BG);


        JLabel backBtn = new JLabel("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        backBtn.setForeground(new Color(255, 220, 180));   
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        backBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(Color.WHITE);
            }
            @Override public void mouseExited(MouseEvent e) {
                backBtn.setForeground(new Color(255, 220, 180));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                backBtn.setEnabled(false);
                new SwingWorker<CatalogPanel, Void>() {
                    @Override
                    protected CatalogPanel doInBackground() {
                        return new CatalogPanel(SessionManager.currentUser);
                    }
                    @Override
                    protected void done() {
                        try {
                            CatalogPanel catalogPanel = get();
                            parentFrame.setContentPane(catalogPanel);
                            parentFrame.revalidate();
                            parentFrame.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            backBtn.setEnabled(true);
                        }
                    }
                }.execute();
            }
        });
        topBar.add(backBtn);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(BG);
        centerWrap.add(buildCard());

        add(topBar, BorderLayout.NORTH);
        add(centerWrap, BorderLayout.CENTER);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 235), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));
        card.setMaximumSize(new Dimension(380, 510));
        card.setPreferredSize(new Dimension(380, 510));

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

        JLabel footer = new JLabel("Don't have an account? Sign up");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(ACCENT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                SigninPanel signinPanel = new SigninPanel(parentFrame);
                parentFrame.setContentPane(signinPanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });

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
