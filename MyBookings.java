import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
        private JSONObject booking;
        
        public BookingCard(JSONObject booking) {
            this.booking = booking;
            
            setLayout(new BorderLayout());
            setMaximumSize(new Dimension(900, 150));
            setBackground(new Color(60, 0, 0));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 0, 0), 2),
                new EmptyBorder(15, 15, 15, 15)
            ));
            
            // Info Panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            int bookingId = booking.getInt("id");
            String seatNumbers = booking.getString("seat_numbers");
            int quantity = booking.getInt("quantity");
            double totalPrice = booking.getDouble("total_price");
            String status = booking.getString("status");
            String bookingDate = booking.optString("booking_date", "N/A");
            
            // Concert info (if available from join)
            String concertName = "Concert";
            try {
                JSONObject concert = booking.optJSONObject("concerts");
                if (concert != null) {
                    concertName = concert.getString("name");
                }
            } catch (Exception e) {
                // Ignore
            }
            
            JLabel nameLabel = new JLabel("🎵 " + concertName);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            nameLabel.setForeground(Color.WHITE);
            
            JLabel detailsLabel = new JLabel(String.format("Seats: %s | Quantity: %d", seatNumbers, quantity));
            detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            detailsLabel.setForeground(new Color(200, 200, 200));
            
            JLabel priceLabel = new JLabel(String.format("Total: ฿%.2f", totalPrice));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            priceLabel.setForeground(new Color(255, 200, 0));
            
            JLabel statusLabel = new JLabel("Status: " + status.toUpperCase());
            statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            statusLabel.setForeground(status.equals("confirmed") ? new Color(0, 255, 0) : new Color(255, 100, 100));
            
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(detailsLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(priceLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(statusLabel);
            
            // Cancel Button
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);
            
            if (status.equals("confirmed")) {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setBackground(new Color(150, 0, 0));
                cancelButton.setFocusPainted(false);
                cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cancelButton.addActionListener(e -> cancelBooking(bookingId));
                buttonPanel.add(cancelButton);
            }
            
            add(infoPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.EAST);
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
