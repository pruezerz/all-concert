import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import org.json.JSONArray;

public class ConcertList extends JFrame {
    private int userId;
    private String username;
    private JPanel concertGridPanel;
    private JTextField searchField;
    
    public ConcertList(int userId, String username) {
        this.userId = userId;
        this.username = username;
        
        setTitle("All Concert - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(100, 20, 20));
        add(mainPanel);
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content Area with ScrollPane
        JScrollPane scrollPane = createContentArea();
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Load concerts
        loadConcerts();
        
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(120, 25, 25));
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        
        // Center: Search Bar
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        
        searchField = new RoundedTextField(20);
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setText("search");
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setCaretColor(Color.WHITE);
        searchField.setBackground(new Color(80, 15, 15));
        
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("search");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        searchField.addActionListener(e -> performSearch());
        
        centerPanel.add(searchField);
        
        // Right: User Menu with icons
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
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JScrollPane createContentArea() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(100, 20, 20));
        
        // Recommended Section - Centered
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(30, 0, 20, 0));
        
        JLabel recommendedTitle = new JLabel("Recommended for You");
        recommendedTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        recommendedTitle.setForeground(Color.WHITE);
        titlePanel.add(recommendedTitle);
        contentPanel.add(titlePanel);
        
        // Concert Grid (3 columns)
        concertGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        concertGridPanel.setBackground(new Color(100, 20, 20));
        concertGridPanel.setBorder(new EmptyBorder(0, 50, 40, 50));
        contentPanel.add(concertGridPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        return scrollPane;
    }
    
    private JPanel createBannerSection() {
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setPreferredSize(new Dimension(1100, 300));
        bannerPanel.setMaximumSize(new Dimension(1100, 300));
        bannerPanel.setBackground(new Color(100, 20, 20));
        bannerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        try {
            // Try to load banner image
            JLabel bannerLabel = new JLabel("🎵 Welcome to All Concert 🎵");
            bannerLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
            bannerLabel.setForeground(Color.WHITE);
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bannerPanel.add(bannerLabel);
        } catch (Exception e) {
            JLabel placeholderLabel = new JLabel("Featured Concerts");
            placeholderLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            placeholderLabel.setForeground(Color.WHITE);
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bannerPanel.add(placeholderLabel);
        }
        
        return bannerPanel;
    }
    
    private void loadConcerts() {
        concertGridPanel.removeAll();
        
        JSONArray concerts = SupabaseConfig.getAllConcerts();
        
        if (concerts.length() == 0) {
            JLabel noDataLabel = new JLabel("No concerts available");
            noDataLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            noDataLabel.setForeground(Color.WHITE);
            concertGridPanel.add(noDataLabel);
        } else {
            for (int i = 0; i < concerts.length(); i++) {
                JSONObject concert = concerts.getJSONObject(i);
                concertGridPanel.add(new ConcertCard(concert));
            }
        }
        
        concertGridPanel.revalidate();
        concertGridPanel.repaint();
    }
    
    private void performSearch() {
        String keyword = searchField.getText();
        if (keyword.isEmpty() || keyword.equals("search")) {
            loadConcerts();
            return;
        }
        
        concertGridPanel.removeAll();
        JSONArray concerts = SupabaseConfig.searchConcerts(keyword);
        
        if (concerts.length() == 0) {
            JLabel noResultLabel = new JLabel("No concerts found for: " + keyword);
            noResultLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            noResultLabel.setForeground(Color.WHITE);
            concertGridPanel.add(noResultLabel);
        } else {
            for (int i = 0; i < concerts.length(); i++) {
                JSONObject concert = concerts.getJSONObject(i);
                concertGridPanel.add(new ConcertCard(concert));
            }
        }
        
        concertGridPanel.revalidate();
        concertGridPanel.repaint();
    }
    
    private void showUserMenu() {
        JPopupMenu userMenu = new JPopupMenu();
        
        JMenuItem myBookingsItem = new JMenuItem("My Bookings");
        myBookingsItem.addActionListener(e -> {
            new MyBookings(userId, username);
            dispose();
        });
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login();
                dispose();
            }
        });
        
        userMenu.add(myBookingsItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);
        
        userMenu.show(this, getWidth() - 150, 70);
    }
    
    // Inner class for Concert Card (vertical layout like in image)
    class ConcertCard extends JPanel {
        private JSONObject concert;
        
        public ConcertCard(JSONObject concert) {
            this.concert = concert;
            
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(300, 380));
            setBackground(new Color(80, 15, 15));
            setBorder(BorderFactory.createLineBorder(new Color(60, 10, 10), 1));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            // Image Panel (Top)
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setPreferredSize(new Dimension(300, 250));
            imagePanel.setBackground(new Color(60, 10, 10));
            
            // Try to load image from URL or file path
            String imageUrl = concert.optString("image_url", "");
            JLabel imageLabel;
            
            if (!imageUrl.isEmpty()) {
                try {
                    ImageIcon icon;
                    if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                        URL url = new URL(imageUrl);
                        Image img = ImageIO.read(url);
                        icon = new ImageIcon(img);
                    } else {
                        icon = new ImageIcon(imageUrl);
                    }
                    
                    Image img = icon.getImage();
                    int originalWidth = img.getWidth(null);
                    int originalHeight = img.getHeight(null);
                    
                    int targetWidth = 300;
                    int targetHeight = 250;
                    
                    double scale = Math.min((double)targetWidth/originalWidth, (double)targetHeight/originalHeight);
                    int scaledWidth = (int)(originalWidth * scale);
                    int scaledHeight = (int)(originalHeight * scale);
                    
                    Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(scaledImg));
                    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                } catch (Exception e) {
                    imageLabel = new JLabel("🎸", SwingConstants.CENTER);
                    imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 80));
                    imageLabel.setForeground(new Color(150, 50, 50));
                }
            } else {
                imageLabel = new JLabel("🎸", SwingConstants.CENTER);
                imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 80));
                imageLabel.setForeground(new Color(150, 50, 50));
            }
            
            imagePanel.add(imageLabel);
            
            // Info Panel (Bottom)
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(80, 15, 15));
            infoPanel.setBorder(new EmptyBorder(12, 15, 15, 15));
            
            String name = concert.getString("name");
            String artist = concert.getString("artist");
            String date = concert.getString("date");
            String venue = concert.getString("venue");
            
            JLabel nameLabel = new JLabel(truncate(name, 35));
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel dateLabel = new JLabel("📅 " + date);
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dateLabel.setForeground(new Color(180, 180, 180));
            dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel venueLabel = new JLabel("📍 " + truncate(venue, 32));
            venueLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            venueLabel.setForeground(new Color(100, 200, 150));
            venueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(6));
            infoPanel.add(dateLabel);
            infoPanel.add(Box.createVerticalStrut(4));
            infoPanel.add(venueLabel);
            
            add(imagePanel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            
            // Hover effect
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    openConcertDetail();
                }
                
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(100, 25, 25));
                    infoPanel.setBackground(new Color(100, 25, 25));
                    setBorder(BorderFactory.createLineBorder(new Color(150, 50, 50), 2));
                }
                
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(80, 15, 15));
                    infoPanel.setBackground(new Color(80, 15, 15));
                    setBorder(BorderFactory.createLineBorder(new Color(60, 10, 10), 1));
                }
            });
        }
        
        private void openConcertDetail() {
            new ConcertDetail(userId, username, concert);
            ConcertList.this.dispose();
        }
        
        private String truncate(String text, int maxLength) {
            if (text.length() <= maxLength) return text;
            return text.substring(0, maxLength - 3) + "...";
        }
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
            g2.setColor(new Color(150, 150, 150));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }
}
