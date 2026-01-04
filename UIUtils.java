import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Utility class for common UI components
 * Reduces code duplication across the application
 */
public class UIUtils {
    
    // Color constants
    public static final Color DARK_RED = new Color(100, 20, 20);
    public static final Color HEADER_RED = new Color(120, 25, 25);
    public static final Color DARK_INPUT = new Color(80, 15, 15);
    public static final Color LIGHT_GRAY = new Color(150, 150, 150);
    public static final Color LIGHT_RED = new Color(210, 180, 180);
    
    // Font constants
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 28);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 24);
    
    /**
     * Creates the logo with fallback text
     * @return JLabel with logo or text
     */
    public static JLabel createLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon("file/logo.png");
            Image img = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            JLabel logoText = new JLabel("ALL CONCERT");
            logoText.setFont(LABEL_FONT);
            logoText.setForeground(Color.WHITE);
            return logoText;
        }
    }
    
    /**
     * Creates an icon button with fallback text
     * @param iconPath path to icon file
     * @param fallbackEmoji fallback emoji/text
     * @param tooltip tooltip text
     * @return JButton configured as icon button
     */
    public static JButton createIconButton(String iconPath, String fallbackEmoji, String tooltip) {
        JButton btn = new JButton();
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText(fallbackEmoji);
        }
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        return btn;
    }
    
    /**
     * Creates a back button
     * @return JButton configured as back button
     */
    public static JButton createBackButton(JFrame parent, int userId, String username) {
        JButton backButton = new JButton("← Back");
        backButton.setFont(BUTTON_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(DARK_RED);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ConcertList(userId, username);
            parent.dispose();
        });
        return backButton;
    }
    
    /**
     * Creates a logout button
     * @param parent parent frame
     * @return JButton configured as logout button
     */
    public static JButton createLogoutButton(JFrame parent) {
        JButton logoutBtn = createIconButton("file/logout.png", "🚪", "Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(parent,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login();
                parent.dispose();
            }
        });
        return logoutBtn;
    }
    
    /**
     * Creates a booking history button
     * @param parent parent frame
     * @param userId user ID
     * @param username username
     * @return JButton configured as booking button
     */
    public static JButton createBookingButton(JFrame parent, int userId, String username) {
        JButton bookingBtn = createIconButton("file/history booking.png", "📋", "My Bookings");
        bookingBtn.addActionListener(e -> {
            new MyBookings(userId, username);
            parent.dispose();
        });
        return bookingBtn;
    }
    
    /**
     * Creates a basic top bar (Logo + Back + Logout)
     * @param parent parent frame
     * @param userId user ID
     * @param username username
     * @return JPanel configured as top bar
     */
    public static JPanel createBasicTopBar(JFrame parent, int userId, String username) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(HEADER_RED);
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo + Back Button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(createLogo());
        leftPanel.add(createBackButton(parent, userId, username));
        
        // Right: Logout icon
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createLogoutButton(parent));
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    /**
     * Creates top bar with Logo + Back + Center Title + Logout
     * @param parent parent frame
     * @param userId user ID
     * @param username username
     * @param title center title text
     * @return JPanel configured as top bar
     */
    public static JPanel createTopBarWithTitle(JFrame parent, int userId, String username, String title) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(HEADER_RED);
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo + Back Button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(createLogo());
        leftPanel.add(createBackButton(parent, userId, username));
        
        // Center: Title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        
        // Right: Logout icon
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createLogoutButton(parent));
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    /**
     * Creates top bar with Logo + Back + Booking + Logout (no center)
     * @param parent parent frame
     * @param userId user ID
     * @param username username
     * @return JPanel configured as top bar
     */
    public static JPanel createTopBarWithBooking(JFrame parent, int userId, String username) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(HEADER_RED);
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo + Back Button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(createLogo());
        leftPanel.add(createBackButton(parent, userId, username));
        
        // Right: Booking + Logout icons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createBookingButton(parent, userId, username));
        rightPanel.add(createLogoutButton(parent));
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
}
