import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
// import javax.swing.border.LineBorder; // No longer needed directly

public class login extends JFrame implements ActionListener {

    // ประกาศ Components ที่จำเป็น
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JLabel messageLabel;

    public login() {
        // ตั้งค่าหน้าต่าง (JFrame)
        super("Login Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove window decorations
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen

        // สร้าง JPanel และใช้ Layout Manager
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Title "ล็อคอิน"
        JLabel titleLabel = new JLabel("Log in");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // 1. Label "Username"
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 1; // Shifted down
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameLabel, gbc);

        // 2. ช่องชื่อผู้ใช้ (Username Field)
        userField = new JTextField(20);
        userField.setPreferredSize(new Dimension(300, 40));
        userField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userField.setBorder(new RoundedBorder(10, Color.LIGHT_GRAY)); // Apply rounded border
        userField.setText("Enter your username"); // Placeholder text
        userField.setForeground(Color.GRAY);
        userField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (userField.getText().equals("Enter your username")) {
                    userField.setText("");
                    userField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (userField.getText().isEmpty()) {
                    userField.setText("Enter your username");
                    userField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 2; // Shifted down
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(userField, gbc);

        // 3. Label "Password"
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = 3; // Shifted down
        mainPanel.add(passwordLabel, gbc);

        // 4. ช่องรหัสผ่าน (Password Field)
        passField = new JPasswordField(20);
        passField.setPreferredSize(new Dimension(300, 40));
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passField.setBorder(new RoundedBorder(10, Color.LIGHT_GRAY)); // Apply rounded border
        passField.setEchoChar((char) 0); // Remove echo character (dots)
        passField.setText("Enter your password"); // Placeholder text
        passField.setForeground(Color.GRAY);
        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(passField.getPassword()).equals("Enter your password")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                    passField.setEchoChar('*'); // Set echo character back when typing
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(passField.getPassword()).isEmpty()) {
                    passField.setEchoChar((char) 0); // Remove echo character when empty
                    passField.setText("Enter your password");
                    passField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 4; // Shifted down
        mainPanel.add(passField, gbc);

        // Adjust gridy for login button after removing checkbox
        // 5. ปุ่ม Login ("Sign in")
        loginButton = new JButton("Sign in");
        loginButton.addActionListener(this);
        loginButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setBorder(new RoundedBorder(10, new Color(30, 144, 255))); // Apply rounded border
        loginButton.setFocusPainted(false); // Remove focus border
        gbc.gridy = 5; // Changed from 4 to 5
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);
        
        // 6. ข้อความสำหรับแสดงผลการล็อกอิน
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 6; // Changed from 5 to 6
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(messageLabel, gbc);

        setVisible(true); // แสดงหน้าต่าง
    }

    // 3. การจัดการเหตุการณ์ (Event Handling) เมื่อผู้ใช้กดปุ่ม
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userField.getText();
            String password = new String(passField.getPassword()); 

            // ตรวจสอบ Username และ Password (ส่วนจำลอง)
            if (username.equals("admin") && password.equals("1234")) {
                messageLabel.setText("Login Successful!");
                messageLabel.setForeground(new Color(34, 139, 34)); // Forest Green
                // โค้ดเพื่อไปยังหน้าถัดไป
            } else {
                messageLabel.setText("Invalid Credentials.");
                messageLabel.setForeground(Color.RED);
            }
        }
    }

    public static void main(String[] args) {
        // รัน GUI บน Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login();
            }
        });
    }
}
