import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import org.json.JSONArray;

public class MyBookings extends JFrame {
    private int userId;
    private String username;
    private JPanel bookingsPanel;
    
    public MyBookings(int userId, String username) {
        this.userId = userId;
        this.username = username;
        
        setTitle("My Bookings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(100, 20, 20));
        add(mainPanel);
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content
        JScrollPane scrollPane = createBookingsPanel();
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        loadBookings();
        
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
        
        // Center: Title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        
        // Right: Logout icon
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
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
        
        rightPanel.add(logoutBtn);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JScrollPane createBookingsPanel() {
        bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBackground(new Color(100, 20, 20));
        bookingsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        return scrollPane;
    }
    
    private void loadBookings() {
        bookingsPanel.removeAll();
        
        JSONArray bookings = SupabaseConfig.getUserBookings(userId);
        
        if (bookings.length() == 0) {
            JLabel noDataLabel = new JLabel("No bookings yet");
            noDataLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
            noDataLabel.setForeground(Color.WHITE);
            bookingsPanel.add(noDataLabel);
        } else {
            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);
                bookingsPanel.add(new BookingCard(booking));
                bookingsPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }
    
    // Inner class for Booking Card
    class BookingCard extends JPanel {
        
        public BookingCard(JSONObject booking) {
            
            setLayout(new BorderLayout(20, 0));
            setMaximumSize(new Dimension(900, 280));
            setBackground(new Color(210, 180, 180));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 50, 50), 3),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            // Get booking data
            int bookingId = booking.getInt("id");
            String bookingRef = booking.optString("booking_reference", "BK" + bookingId);
            String seatNumbers = booking.getString("seat_numbers");
            int quantity = booking.getInt("quantity");
            double totalPrice = booking.getDouble("total_price");
            String status = booking.getString("status");
            
            // Concert info (if available from join)
            String concertName = "Concert";
            String artistName = "";
            String imageUrl = "";
            String zone = "N/A";
            
            try {
                JSONObject concert = booking.optJSONObject("concerts");
                if (concert != null) {
                    concertName = concert.optString("name", "Concert");
                    artistName = concert.optString("artist", "");
                    imageUrl = concert.optString("image_url", "");
                }
            } catch (Exception e) {
                // Ignore
            }
            
            // Extract zone from seat numbers (e.g., "BLUE-B2" -> "BLUE")
            if (seatNumbers.contains("-")) {
                zone = seatNumbers.split("-")[0];
            }
            
            // LEFT PANEL: Concert Image with ticket info overlay
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setPreferredSize(new Dimension(180, 260));
            leftPanel.setBackground(new Color(40, 40, 40));
            leftPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
            
            // Image
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            
            if (!imageUrl.isEmpty()) {
                try {
                    URL url = new java.net.URI(imageUrl).toURL();
                    ImageIcon icon = new ImageIcon(url);
                    Image img = icon.getImage().getScaledInstance(176, 210, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    imageLabel.setText("🎸");
                    imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 60));
                    imageLabel.setForeground(Color.WHITE);
                }
            } else {
                imageLabel.setText("🎸");
                imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 60));
                imageLabel.setForeground(Color.WHITE);
            }
            
            leftPanel.add(imageLabel, BorderLayout.CENTER);
            
            // Ticket info overlay at bottom
            JPanel ticketInfoPanel = new JPanel(new GridLayout(1, 2, 5, 0));
            ticketInfoPanel.setBackground(new Color(40, 40, 40, 230));
            ticketInfoPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
            
            JLabel ticketLabel = new JLabel(quantity + " Ticket" + (quantity > 1 ? "s" : ""));
            ticketLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            ticketLabel.setForeground(Color.WHITE);
            
            JLabel zoneLabel = new JLabel("ZONE: " + zone);
            zoneLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
            zoneLabel.setForeground(new Color(255, 200, 100));
            zoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            ticketInfoPanel.add(ticketLabel);
            ticketInfoPanel.add(zoneLabel);
            
            leftPanel.add(ticketInfoPanel, BorderLayout.SOUTH);
            
            // RIGHT PANEL: QR Code and booking details
            JPanel rightPanel = new JPanel(new BorderLayout(15, 0));
            rightPanel.setOpaque(false);
            
            // QR Code Panel
            JPanel qrPanel = new JPanel(new BorderLayout());
            qrPanel.setPreferredSize(new Dimension(160, 210));
            qrPanel.setBackground(Color.WHITE);
            qrPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 50, 50), 2),
                new EmptyBorder(10, 10, 10, 10)
            ));
            
            // QR Code label
            JLabel qrLabel = new JLabel("QR CODE", SwingConstants.CENTER);
            qrLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            qrLabel.setForeground(new Color(100, 20, 20));
            qrPanel.add(qrLabel, BorderLayout.NORTH);
            
            // Generate QR Code using QR Server API
            JLabel qrImageLabel = new JLabel("Loading...");
            qrImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrImageLabel.setVerticalAlignment(SwingConstants.CENTER);
            qrImageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            qrImageLabel.setForeground(new Color(100, 20, 20));
            
            qrPanel.add(qrImageLabel, BorderLayout.CENTER);
            
            // Load QR Code in background thread
            new Thread(() -> {
                try {
                    String encodedRef = java.net.URLEncoder.encode(bookingRef, "UTF-8");
                    // Try multiple QR code services
                    String[] qrUrls = {
                        "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + encodedRef,
                        "https://chart.googleapis.com/chart?cht=qr&chs=150x150&chl=" + encodedRef
                    };
                    
                    boolean loaded = false;
                    for (String qrCodeUrl : qrUrls) {
                        try {
                            java.net.URL url = new java.net.URI(qrCodeUrl).toURL();
                            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(3000);
                            connection.setReadTimeout(3000);
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                            connection.connect();
                            
                            if (connection.getResponseCode() == 200) {
                                java.awt.image.BufferedImage qrImage = javax.imageio.ImageIO.read(connection.getInputStream());
                                
                                if (qrImage != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        qrImageLabel.setIcon(new ImageIcon(qrImage));
                                        qrImageLabel.setText(null);
                                    });
                                    loaded = true;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            // Try next URL
                            continue;
                        }
                    }
                    
                    if (!loaded) {
                        SwingUtilities.invokeLater(() -> {
                            qrImageLabel.setText("<html><center><b>" + bookingRef + "</b></center></html>");
                            qrImageLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                        });
                    }
                } catch (Exception e) {
                    // Fallback: show booking reference as text
                    SwingUtilities.invokeLater(() -> {
                        qrImageLabel.setText("<html><center><b>" + bookingRef + "</b></center></html>");
                        qrImageLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                    });
                }
            }).start();
            
            rightPanel.add(qrPanel, BorderLayout.EAST);
            
            // Booking Details Panel
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setOpaque(false);
            detailsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
            
            // Concert name and artist
            JLabel nameLabel = new JLabel(concertName);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            nameLabel.setForeground(new Color(100, 20, 20));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            if (!artistName.isEmpty()) {
                JLabel artistLabel = new JLabel(artistName);
                artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                artistLabel.setForeground(new Color(80, 80, 80));
                artistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                detailsPanel.add(nameLabel);
                detailsPanel.add(Box.createVerticalStrut(3));
                detailsPanel.add(artistLabel);
            } else {
                detailsPanel.add(nameLabel);
            }
            
            detailsPanel.add(Box.createVerticalStrut(15));
            
            // Booking info grid
            JPanel infoGrid = new JPanel(new GridLayout(2, 2, 15, 10));
            infoGrid.setOpaque(false);
            infoGrid.setMaximumSize(new Dimension(400, 80));
            infoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            infoGrid.add(createInfoLabel("Name:", username));
            infoGrid.add(createInfoLabel("BookingNo:", bookingRef));
            infoGrid.add(createInfoLabel("SEAT:", seatNumbers));
            infoGrid.add(createInfoLabel("Quantity:", quantity + " seat(s)"));
            
            detailsPanel.add(infoGrid);
            detailsPanel.add(Box.createVerticalStrut(10));
            
            // Price and Status
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            bottomPanel.setOpaque(false);
            bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel priceLabel = new JLabel(String.format("฿%.2f", totalPrice));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            priceLabel.setForeground(new Color(0, 150, 0));
            
            JLabel statusLabel = new JLabel("● " + status.toUpperCase());
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            statusLabel.setForeground(status.equals("confirmed") ? new Color(0, 180, 0) : new Color(200, 0, 0));
            
            bottomPanel.add(priceLabel);
            bottomPanel.add(Box.createHorizontalStrut(10));
            bottomPanel.add(statusLabel);
            
            // Cancel button if confirmed
            if (status.equals("confirmed")) {
                JButton cancelButton = new JButton("Cancel Booking");
                cancelButton.setFont(new Font("SansSerif", Font.BOLD, 11));
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setBackground(new Color(180, 50, 50));
                cancelButton.setFocusPainted(false);
                cancelButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 30, 30), 1),
                    new EmptyBorder(5, 15, 5, 15)
                ));
                cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cancelButton.addActionListener(e -> cancelBooking(bookingId));
                bottomPanel.add(Box.createHorizontalStrut(20));
                bottomPanel.add(cancelButton);
            }
            
            detailsPanel.add(bottomPanel);
            
            rightPanel.add(detailsPanel, BorderLayout.CENTER);
            
            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.CENTER);
        }
        
        private JPanel createInfoLabel(String label, String value) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);
            
            JLabel labelText = new JLabel(label);
            labelText.setFont(new Font("SansSerif", Font.BOLD, 11));
            labelText.setForeground(new Color(120, 120, 120));
            labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel valueText = new JLabel(value);
            valueText.setFont(new Font("SansSerif", Font.BOLD, 13));
            valueText.setForeground(new Color(60, 60, 60));
            valueText.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            panel.add(labelText);
            panel.add(valueText);
            
            return panel;
        }
        
        private void cancelBooking(int bookingId) {
            int confirm = JOptionPane.showConfirmDialog(MyBookings.this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                JSONObject result = SupabaseConfig.cancelBooking(bookingId);
                
                if (result.has("error") && result.getBoolean("error")) {
                    JOptionPane.showMessageDialog(MyBookings.this,
                        "Cancellation failed: " + result.getString("message"),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MyBookings.this,
                        "Booking cancelled successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadBookings();
                }
            }
        }
    }
}
