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
        JPanel contentPanel = new JPanel(new BorderLayout(40, 40));
        contentPanel.setBackground(new Color(100, 20, 20));
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        // Left Side: Concert Image
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(100, 20, 20));
        leftPanel.setPreferredSize(new Dimension(500, 700));
        
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(500, 650));
        imagePanel.setBackground(new Color(60, 10, 10));
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 50, 50), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Try to load image from URL or file path
        String imageUrl = concert.optString("image_url", "");
        
        if (!imageUrl.isEmpty()) {
            try {
                ImageIcon icon;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    URL url = new java.net.URI(imageUrl).toURL();
                    Image img = ImageIO.read(url);
                    icon = new ImageIcon(img);
                } else {
                    icon = new ImageIcon(imageUrl);
                }
                
                Image img = icon.getImage();
                int originalWidth = img.getWidth(null);
                int originalHeight = img.getHeight(null);
                
                int targetWidth = 480;
                int targetHeight = 630;
                
                double scale = Math.min((double)targetWidth/originalWidth, (double)targetHeight/originalHeight);
                int scaledWidth = (int)(originalWidth * scale);
                int scaledHeight = (int)(originalHeight * scale);
                
                Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel);
            } catch (Exception e) {
                JLabel imagePlaceholder = new JLabel("🎸", SwingConstants.CENTER);
                imagePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 120));
                imagePlaceholder.setForeground(new Color(150, 150, 150));
                imagePanel.add(imagePlaceholder);
            }
        } else {
            JLabel imagePlaceholder = new JLabel("🎸", SwingConstants.CENTER);
            imagePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 120));
            imagePlaceholder.setForeground(new Color(150, 150, 150));
            imagePanel.add(imagePlaceholder);
        }
        
        leftPanel.add(imagePanel, BorderLayout.CENTER);
        
        // Right Side: Concert Info
        String name = concert.getString("name");
        String artist = concert.getString("artist");
        String date = concert.getString("date");
        String venue = concert.getString("venue");
        double price = concert.getDouble("price");
        int seatsAvailable = concert.getInt("seats_available");
        String description = concert.optString("description", "No description available");
        
        JScrollPane rightScrollPane = new JScrollPane();
        rightScrollPane.setBorder(null);
        rightScrollPane.setBackground(new Color(100, 20, 20));
        rightScrollPane.getViewport().setBackground(new Color(100, 20, 20));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(100, 20, 20));
        rightPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Concert Name
        JLabel nameLabel = new JLabel("<html>" + name.toUpperCase() + "</html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        nameLabel.setForeground(new Color(255, 220, 100));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Artist
        JLabel artistLabel = new JLabel("🎤 " + artist);
        artistLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        artistLabel.setForeground(new Color(200, 200, 200));
        artistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        artistLabel.setBorder(new EmptyBorder(10, 0, 30, 0));
        
        rightPanel.add(nameLabel);
        rightPanel.add(artistLabel);
        
        // Info Cards - 2 columns
        JPanel infoGrid = new JPanel(new GridLayout(3, 2, 20, 15));
        infoGrid.setOpaque(false);
        infoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoGrid.setMaximumSize(new Dimension(600, 180));
        
        infoGrid.add(createInfoCard("📅 DATE", date));
        infoGrid.add(createInfoCard("📍 VENUE", venue));
        infoGrid.add(createInfoCard("🕐 TIME", "20:00 - 01:00"));
        infoGrid.add(createInfoCard("💰 PRICE", String.format("฿%.0f", price)));
        infoGrid.add(createInfoCard("💺 AVAILABLE", seatsAvailable + " seats"));
        infoGrid.add(createInfoCard("🎫 ZONES", "8 zones"));
        
        rightPanel.add(infoGrid);
        rightPanel.add(Box.createVerticalStrut(30));
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(120, 25, 25));
        separator.setMaximumSize(new Dimension(600, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(separator);
        rightPanel.add(Box.createVerticalStrut(25));
        
        // Book Button
        JButton bookButton = new JButton("🎫 SELECT SEATS & BOOK NOW");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        bookButton.setForeground(new Color(60, 10, 10));
        bookButton.setBackground(new Color(255, 200, 50));
        bookButton.setFocusPainted(false);
        bookButton.setPreferredSize(new Dimension(400, 65));
        bookButton.setMaximumSize(new Dimension(400, 65));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 150, 0), 2),
            new EmptyBorder(5, 20, 5, 20)
        ));
        bookButton.addActionListener(e -> {
            new SeatSelection(userId, username, concert);
            dispose();
        });
        
        bookButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bookButton.setBackground(new Color(255, 220, 100));
            }
            public void mouseExited(MouseEvent e) {
                bookButton.setBackground(new Color(255, 200, 50));
            }
        });
        
        rightPanel.add(bookButton);
        
        // Description Section
        JLabel descriptionHeader = new JLabel("DESCRIPTION");
        descriptionHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        descriptionHeader.setForeground(new Color(255, 220, 100));
        descriptionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionHeader.setBorder(new EmptyBorder(30, 0, 10, 0));
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descArea.setForeground(new Color(220, 220, 220));
        descArea.setBackground(new Color(100, 20, 20));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(new EmptyBorder(0, 0, 30, 0));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(descriptionHeader);
        rightPanel.add(descArea);
        
        rightScrollPane.setViewportView(rightPanel);
        
        // Add to main content panel
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightScrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createInfoCard(String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(80, 15, 15));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 25, 25), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SansSerif", Font.BOLD, 11));
        labelText.setForeground(new Color(150, 150, 150));
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueText = new JLabel("<html>" + value + "</html>");
        valueText.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueText.setForeground(Color.WHITE);
        valueText.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueText.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        card.add(labelText);
        card.add(valueText);
        
        return card;
    }
}
