import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class Login extends JFrame {

    public Login() {
        setTitle("All Concert - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Main Panel with Dark Red Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(80, 0, 0)); // Dark Red
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo
        try {
            // Assuming logo is in file/logo.png relative to project root
            ImageIcon logoIcon = new ImageIcon("file/logo.png");
            Image img = logoIcon.getImage();
            // Keep aspect ratio - scale to max width/height of 150px
            int originalWidth = img.getWidth(null);
            int originalHeight = img.getHeight(null);
            int maxSize = 150;
            double scale = Math.min((double)maxSize/originalWidth, (double)maxSize/originalHeight);
            int scaledWidth = (int)(originalWidth * scale);
            int scaledHeight = (int)(originalHeight * scale);
            Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH); 
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
            mainPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Logo not found");
            errorLabel.setForeground(Color.WHITE);
            mainPanel.add(errorLabel, gbc);
        }

        // "Log In" Title
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(10, 10, 30, 10);
        mainPanel.add(titleLabel, gbc);

        // Username Label
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        // Create a container for the form to align items nicely
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(10));

        // Username Field
        JTextField userField = new RoundedTextField(20);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBackground(new Color(80, 0, 0)); // Match background
        userField.setText("Username"); // Placeholder
        userField.setMaximumSize(new Dimension(400, 50));
        userField.setPreferredSize(new Dimension(400, 50));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(userField);
        
        formPanel.add(Box.createVerticalStrut(20));

        // Password Label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(10));

        // Password Field
        JPasswordField passField = new RoundedPasswordField(20);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBackground(new Color(80, 0, 0));
        passField.setText("Password"); // Placeholder
        passField.setEchoChar((char)0); // Show text initially
        passField.setMaximumSize(new Dimension(400, 50));
        passField.setPreferredSize(new Dimension(400, 50));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formPanel.add(passField);

        formPanel.add(Box.createVerticalStrut(40));

        // Login Button
        JButton loginButton = new RoundedButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        loginButton.setForeground(new Color(80, 0, 0));
        loginButton.setBackground(Color.WHITE);
        loginButton.setMaximumSize(new Dimension(200, 50));
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login Action
        ActionListener loginAction = e -> {
            String username = userField.getText();
            String password = String.valueOf(passField.getPassword());
            
            if (username.isEmpty() || username.equals("Username")) {
                JOptionPane.showMessageDialog(this, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.isEmpty() || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Disable button and show loading
            loginButton.setEnabled(false);
            loginButton.setText("Logging in...");
            
            // Perform login in background thread
            new Thread(() -> {
                JSONObject result = SupabaseConfig.loginUser(username, password);
                
                SwingUtilities.invokeLater(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                    
                    if (result.has("error") && result.getBoolean("error")) {
                        JOptionPane.showMessageDialog(this, result.getString("message"), "Login Failed", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String role = result.optString("role", "user");
                        int userId = result.getInt("id");
                        String userName = result.getString("username");
                        
                        // Navigate based on role
                        if (role.equals("admin")) {
                            new AdminDashboard(userId, userName);
                            this.dispose();
                        } else {
                            new ConcertList(userId, userName);
                            this.dispose();
                        }
                    }
                });
            }).start();
        };
        
        loginButton.addActionListener(loginAction);
        
        // Add Enter key listener to username and password fields
        userField.addActionListener(loginAction);
        passField.addActionListener(loginAction);
        
        formPanel.add(loginButton);

        formPanel.add(Box.createVerticalStrut(30));

        // Footer Text
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel footerText = new JLabel("If you don't have an account yet, ");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        footerText.setForeground(Color.WHITE);
        
        JLabel registerLink = new JLabel("Register");
        registerLink.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerLink.setForeground(Color.WHITE);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Register Link Action
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Register();
                dispose();
            }
        });
        
        footerPanel.add(footerText);
        footerPanel.add(registerLink);
        formPanel.add(footerPanel);

        mainPanel.add(formPanel, gbc);

        // Placeholder Logic
        userField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (userField.getText().equals("Username")) {
                    userField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (userField.getText().isEmpty()) {
                    userField.setText("Username");
                }
            }
        });

        passField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passField.getPassword()).equals("Password")) {
                    passField.setText("");
                    passField.setEchoChar('•');
                }
            }
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passField.getPassword()).isEmpty()) {
                    passField.setText("Password");
                    passField.setEchoChar((char)0);
                }
            }
        });

        setVisible(true);
    }

    // Custom Rounded TextField
    class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    // Custom Rounded PasswordField
    class RoundedPasswordField extends JPasswordField {
        private int radius;
        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    // Custom Rounded Button
    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
