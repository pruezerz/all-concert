import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import org.json.JSONObject;

public class Register extends JFrame {

    public Register() {
        setTitle("All Concert - Register");
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
            ImageIcon logoIcon = new ImageIcon("file/logo.png");
            Image img = logoIcon.getImage();
            // Keep aspect ratio
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
            JLabel errorLabel = new JLabel("ALL CONCERT");
            errorLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            errorLabel.setForeground(Color.WHITE);
            mainPanel.add(errorLabel, gbc);
        }

        // "Register" Title
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(10, 10, 30, 10);
        mainPanel.add(titleLabel, gbc);

        // Form Panel with 2 columns
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        // Username (Left column)
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        formPanel.add(userLabel, formGbc);

        formGbc.gridy++;
        JTextField userField = new RoundedTextField(20);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBackground(new Color(80, 0, 0));
        userField.setText("Username");
        userField.setPreferredSize(new Dimension(240, 50));
        formPanel.add(userField, formGbc);

        // Password (Right column)
        formGbc.gridx = 1;
        formGbc.gridy = 0;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passLabel.setForeground(Color.WHITE);
        formPanel.add(passLabel, formGbc);

        formGbc.gridy++;
        JPasswordField passField = new RoundedPasswordField(20);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBackground(new Color(80, 0, 0));
        passField.setText("Password");
        passField.setEchoChar((char)0);
        passField.setPreferredSize(new Dimension(240, 50));
        formPanel.add(passField, formGbc);

        // Gender (Left column)
        formGbc.gridx = 0;
        formGbc.gridy = 2;
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        genderLabel.setForeground(Color.WHITE);
        formPanel.add(genderLabel, formGbc);

        formGbc.gridy++;
        JComboBox<String> genderCombo = new RoundedComboBox(new String[]{"Gender", "Male", "Female", "Other"});
        genderCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        genderCombo.setForeground(Color.WHITE);
        genderCombo.setBackground(new Color(80, 0, 0));
        genderCombo.setPreferredSize(new Dimension(240, 50));
        formPanel.add(genderCombo, formGbc);

        // Date of Birth (Right column)
        formGbc.gridx = 1;
        formGbc.gridy = 2;
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dobLabel.setForeground(Color.WHITE);
        formPanel.add(dobLabel, formGbc);

        formGbc.gridy++;
        JTextField dobField = new RoundedTextField(20);
        dobField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dobField.setForeground(Color.WHITE);
        dobField.setCaretColor(Color.WHITE);
        dobField.setBackground(new Color(80, 0, 0));
        dobField.setText("dd/mm/yyyy");
        dobField.setPreferredSize(new Dimension(240, 50));
        
        // Auto-format date with slashes
        dobField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (dobField.getText().equals("dd/mm/yyyy")) {
                    dobField.setText("");
                }
                char c = e.getKeyChar();
                String text = dobField.getText();
                
                // Only allow numbers and prevent more than 10 chars (dd/mm/yyyy)
                if (!Character.isDigit(c) || text.length() >= 10) {
                    e.consume();
                    return;
                }
                
                // Auto-add slashes
                if (text.length() == 2 || text.length() == 5) {
                    dobField.setText(text + "/");
                }
            }
        });
        
        formPanel.add(dobField, formGbc);

        mainPanel.add(formPanel, gbc);

        // Register Button
        gbc.gridy++;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton registerButton = new RoundedButton("Register");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        registerButton.setForeground(new Color(80, 0, 0));
        registerButton.setBackground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(200, 50));
        
        // Register Action
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = String.valueOf(passField.getPassword());
            String gender = (String) genderCombo.getSelectedItem();
            String birthDate = dobField.getText();
            
            // Validation
            if (username.isEmpty() || username.equals("Username")) {
                JOptionPane.showMessageDialog(this, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.isEmpty() || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gender == null || gender.equals("Gender")) {
                JOptionPane.showMessageDialog(this, "Please select gender", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (birthDate.isEmpty() || birthDate.equals("dd/mm/yyyy")) {
                JOptionPane.showMessageDialog(this, "Please enter date of birth", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate date format
            if (!birthDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use dd/mm/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if username exists
            if (SupabaseConfig.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Register user in Supabase
            JSONObject result = SupabaseConfig.registerUser(username, password, gender, birthDate);
            
            if (result.has("error") && result.getBoolean("error")) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + result.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration successful!\nYou can now login", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Login();
                dispose();
            }
        });
        
        mainPanel.add(registerButton, gbc);

        // Footer Text
        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        
        JLabel footerText = new JLabel("If you have an account yet, ");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        footerText.setForeground(Color.WHITE);
        
        JLabel loginLink = new JLabel("Login");
        loginLink.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginLink.setForeground(Color.WHITE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Login Link Action
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Login();
                dispose();
            }
        });
        
        footerPanel.add(loginLink);
        footerPanel.add(loginLink);
        mainPanel.add(footerPanel, gbc);

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

        dobField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (dobField.getText().equals("dd/mm/yyyy")) {
                    dobField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (dobField.getText().isEmpty()) {
                    dobField.setText("dd/mm/yyyy");
                }
                // Validate date format
                else if (!dobField.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
                    JOptionPane.showMessageDialog(Register.this, "Invalid date format. Use dd/mm/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                    dobField.setText("dd/mm/yyyy");
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

    // Custom Rounded ComboBox
    class RoundedComboBox extends JComboBox<String> {
        public RoundedComboBox(String[] items) {
            super(items);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
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
        SwingUtilities.invokeLater(() -> new Register());
    }
}
