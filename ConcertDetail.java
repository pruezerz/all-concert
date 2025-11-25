import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class ConcertDetail extends JFrame {
    private int userId;
    private String username;
    private JSONObject concert;
    
    public ConcertDetail(int userId, String username, JSONObject concert) {
        this.userId = userId;
        this.username = username;
        this.concert = concert;
        
        setTitle("Concert Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(100, 20, 20));
        add(mainPanel);
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(120, 25, 25));
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo + Back Button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        try {
            ImageIcon logoIcon = new ImageIcon("file/logo.png");
            Image img = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(img));
            leftPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel logoText = new JLabel("ALL CONCERT");
            logoText.setFont(new Font("SansSerif", Font.BOLD, 24));
            logoText.setForeground(Color.WHITE);
            leftPanel.add(logoText);
        }
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(100, 20, 20));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ConcertList(userId, username);
            dispose();
        });
        leftPanel.add(backButton);
        
        // Right: Booking History + Logout icons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        // Booking History Button
        JButton bookingBtn = new JButton();
        try {
            ImageIcon bookingIcon = new ImageIcon("file/history booking.png");
            Image img = bookingIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            bookingBtn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            bookingBtn.setText("📋");
        }
        bookingBtn.setBorderPainted(false);
        bookingBtn.setContentAreaFilled(false);
        bookingBtn.setFocusPainted(false);
        bookingBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookingBtn.setToolTipText("My Bookings");
        bookingBtn.addActionListener(e -> {
            new MyBookings(userId, username);
            dispose();
        });
        
        // Logout Button
        JButton logoutBtn = new JButton();
        try {
            ImageIcon logoutIcon = new ImageIcon("file/logout.png");
            Image img = logoutIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            logoutBtn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoutBtn.setText("🚪");
        }
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login();
                dispose();
            }
        });
        
        rightPanel.add(bookingBtn);
        rightPanel.add(logoutBtn);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(100, 20, 20));
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        // Concert Image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(800, 300));
        imagePanel.setMaximumSize(new Dimension(800, 300));
        imagePanel.setBackground(new Color(40, 0, 0));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 2));
        
        // Try to load image from URL or file path
        String imageUrl = concert.optString("image_url", "");
        
        if (!imageUrl.isEmpty()) {
            try {
                ImageIcon icon;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    // Load from URL
                    URL url = new URL(imageUrl);
                    Image img = ImageIO.read(url);
                    icon = new ImageIcon(img);
                } else {
                    // Load from file path
                    icon = new ImageIcon(imageUrl);
                }
                
                // Scale image to fit while maintaining aspect ratio
                Image img = icon.getImage();
                int originalWidth = img.getWidth(null);
                int originalHeight = img.getHeight(null);
                
                int targetWidth = 800;
                int targetHeight = 300;
                
                double scale = Math.min((double)targetWidth/originalWidth, (double)targetHeight/originalHeight);
                int scaledWidth = (int)(originalWidth * scale);
                int scaledHeight = (int)(originalHeight * scale);
                
                Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel);
            } catch (Exception e) {
                // If image fails to load, show default placeholder
                JLabel imagePlaceholder = new JLabel("🎸 Concert Poster", SwingConstants.CENTER);
                imagePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 48));
                imagePlaceholder.setForeground(Color.WHITE);
                imagePanel.add(imagePlaceholder);
            }
        } else {
            // No image URL provided, show default placeholder
            JLabel imagePlaceholder = new JLabel("🎸 Concert Poster", SwingConstants.CENTER);
            imagePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 48));
            imagePlaceholder.setForeground(Color.WHITE);
            imagePanel.add(imagePlaceholder);
        }
        
        contentPanel.add(imagePanel);
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Concert Info - Two Column Layout
        String name = concert.getString("name");
        String artist = concert.getString("artist");
        String date = concert.getString("date");
        String venue = concert.getString("venue");
        double price = concert.getDouble("price");
        int seatsAvailable = concert.getInt("seats_available");
        String description = concert.optString("description", "No description available");
        
        // Title
        JLabel nameLabel = new JLabel(name.toUpperCase());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Info Panel with two columns
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 40, 15));
        infoPanel.setOpaque(false);
        infoPanel.setMaximumSize(new Dimension(800, 150));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left column - Date, Time, Venue
        JLabel dateLabel = new JLabel("📅 " + date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(100, 255, 100));
        
        JLabel timeLabel = new JLabel("🕐 08:00 PM - 01:00 AM");
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        timeLabel.setForeground(new Color(100, 255, 100));
        
        JLabel venueLabel = new JLabel("📍 " + venue);
        venueLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        venueLabel.setForeground(new Color(100, 255, 100));
        
        // Right column - Price (empty space for layout)
        JLabel priceLabel = new JLabel(String.format("฿%.0f", price));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        infoPanel.add(dateLabel);
        infoPanel.add(new JLabel()); // spacer
        infoPanel.add(timeLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(venueLabel);
        infoPanel.add(new JLabel()); // spacer
        
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Concert Details Section
        JLabel detailsHeader = new JLabel("CONCERT DETAILS");
        detailsHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        detailsHeader.setForeground(Color.WHITE);
        detailsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(detailsHeader);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Description with better formatting
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descArea.setForeground(new Color(220, 220, 220));
        descArea.setBackground(new Color(100, 20, 20));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(new EmptyBorder(0, 0, 0, 0));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descArea.setMaximumSize(new Dimension(800, 200));
        
        contentPanel.add(descArea);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Booking Panel
        contentPanel.add(createBookingPanel(price, seatsAvailable));
        
        return contentPanel;
    }
    
    private JPanel createBookingPanel(double price, int maxSeats) {
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        bookingPanel.setBackground(new Color(100, 20, 20));
        bookingPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        bookingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookingPanel.setMaximumSize(new Dimension(800, 150));
        
        JButton bookButton = new JButton("Select Seats");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBackground(new Color(200, 50, 50));
        bookButton.setFocusPainted(false);
        bookButton.setPreferredSize(new Dimension(250, 60));
        bookButton.setMaximumSize(new Dimension(250, 60));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(e -> {
            new SeatSelection(userId, username, concert);
            dispose();
        });
        
        bookingPanel.add(bookButton);
        
        return bookingPanel;
    }
}
