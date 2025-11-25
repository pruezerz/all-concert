import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import org.json.JSONObject;

public class PaymentPage extends JFrame {
    private int userId;
    private String username;
    private JSONObject concert;
    private String selectedZone;
    private Set<String> selectedSeats;
    private double totalPrice;
    private String bookingReference;
    
    public PaymentPage(int userId, String username, JSONObject concert, String selectedZone, Set<String> selectedSeats, double totalPrice) {
        this.userId = userId;
        this.username = username;
        this.concert = concert;
        this.selectedZone = selectedZone;
        this.selectedSeats = selectedSeats;
        this.totalPrice = totalPrice;
        this.bookingReference = generateBookingReference();
        
        setTitle("Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(100, 20, 20));
        add(mainPanel);
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(40, 40));
        contentPanel.setBackground(new Color(100, 20, 20));
        contentPanel.setBorder(new EmptyBorder(40, 80, 40, 80));
        
        // Left: Payment Details
        contentPanel.add(createPaymentDetails(), BorderLayout.WEST);
        
        // Right: QR Code
        contentPanel.add(createQRCodePanel(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(120, 25, 25));
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
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
        
        JLabel titleLabel = new JLabel("← PAYMENT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
            }
        });
        leftPanel.add(titleLabel);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        
        return topBar;
    }
    
    private JPanel createPaymentDetails() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(80, 15, 15));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setPreferredSize(new Dimension(450, 600));
        
        // Title
        JLabel titleLabel = new JLabel("Payment Summary");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));
        
        // Booking Reference
        panel.add(createDetailRow("Booking Reference:", bookingReference));
        panel.add(Box.createVerticalStrut(15));
        
        // Concert Name
        panel.add(createDetailRow("Concert:", concert.getString("name")));
        panel.add(Box.createVerticalStrut(15));
        
        // Artist
        panel.add(createDetailRow("Artist:", concert.getString("artist")));
        panel.add(Box.createVerticalStrut(15));
        
        // Date
        panel.add(createDetailRow("Date:", concert.getString("date")));
        panel.add(Box.createVerticalStrut(15));
        
        // Venue
        panel.add(createDetailRow("Venue:", concert.getString("venue")));
        panel.add(Box.createVerticalStrut(15));
        
        // Zone
        panel.add(createDetailRow("Zone:", selectedZone));
        panel.add(Box.createVerticalStrut(15));
        
        // Seats
        String seatNumbers = String.join(", ", selectedSeats);
        if (seatNumbers.length() > 40) {
            seatNumbers = seatNumbers.substring(0, 40) + "...";
        }
        panel.add(createDetailRow("Seats:", seatNumbers));
        panel.add(Box.createVerticalStrut(15));
        
        // Quantity
        panel.add(createDetailRow("Quantity:", selectedSeats.size() + " seat(s)"));
        panel.add(Box.createVerticalStrut(30));
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.WHITE);
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(20));
        
        // Total Price
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pricePanel.setOpaque(false);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.setMaximumSize(new Dimension(400, 50));
        
        JLabel totalLabel = new JLabel("Total Amount:");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        totalLabel.setForeground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(String.format("  ฿%.2f", totalPrice));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        priceLabel.setForeground(new Color(100, 255, 100));
        
        pricePanel.add(totalLabel);
        pricePanel.add(priceLabel);
        panel.add(pricePanel);
        
        return panel;
    }
    
    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(400, 30));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelText.setForeground(new Color(200, 200, 200));
        labelText.setPreferredSize(new Dimension(180, 25));
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueText.setForeground(Color.WHITE);
        
        row.add(labelText);
        row.add(valueText);
        
        return row;
    }
    
    private JPanel createQRCodePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(100, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Scan QR Code to Pay");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // QR Code (Mock)
        JPanel qrPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // White background
                g2.setColor(Color.WHITE);
                g2.fillRect(20, 20, 360, 360);
                
                // Mock QR pattern
                g2.setColor(Color.BLACK);
                Random rand = new Random(bookingReference.hashCode()); // Use hash for consistent pattern
                int blockSize = 12;
                for (int i = 0; i < 30; i++) {
                    for (int j = 0; j < 30; j++) {
                        if (rand.nextBoolean()) {
                            g2.fillRect(20 + i * blockSize, 20 + j * blockSize, blockSize, blockSize);
                        }
                    }
                }
                
                // Corner markers
                g2.setColor(Color.BLACK);
                drawCornerMarker(g2, 30, 30);
                drawCornerMarker(g2, 310, 30);
                drawCornerMarker(g2, 30, 310);
            }
            
            private void drawCornerMarker(Graphics2D g2, int x, int y) {
                g2.fillRect(x, y, 50, 50);
                g2.setColor(Color.WHITE);
                g2.fillRect(x + 10, y + 10, 30, 30);
                g2.setColor(Color.BLACK);
                g2.fillRect(x + 15, y + 15, 20, 20);
            }
        };
        qrPanel.setBackground(new Color(80, 15, 15));
        qrPanel.setPreferredSize(new Dimension(400, 400));
        qrPanel.setMaximumSize(new Dimension(400, 400));
        qrPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(qrPanel);
        panel.add(Box.createVerticalStrut(30));
        
        // Payment URL (clickable)
        String paymentUrl = "http://localhost:8080/payment?ref=" + bookingReference;
        JLabel urlLabel = new JLabel("<html><u>" + "Click here to open payment page" + "</u></html>");
        urlLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        urlLabel.setForeground(new Color(100, 150, 255));
        urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        urlLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openPaymentWindow();
            }
        });
        panel.add(urlLabel);
        panel.add(Box.createVerticalStrut(30));
        
        // Check Payment Button
        JButton checkBtn = new JButton("Check Payment Status");
        checkBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        checkBtn.setPreferredSize(new Dimension(250, 50));
        checkBtn.setMaximumSize(new Dimension(250, 50));
        checkBtn.setBackground(new Color(255, 200, 50));
        checkBtn.setForeground(Color.BLACK);
        checkBtn.setFocusPainted(false);
        checkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBtn.addActionListener(e -> checkPaymentStatus());
        panel.add(checkBtn);
        
        return panel;
    }
    
    private void openPaymentWindow() {
        // Build payment URL with all parameters
        try {
            String concertName = java.net.URLEncoder.encode(concert.getString("name"), "UTF-8");
            String artist = java.net.URLEncoder.encode(concert.getString("artist"), "UTF-8");
            String date = concert.getString("date");
            String venue = java.net.URLEncoder.encode(concert.getString("venue"), "UTF-8");
            String seatList = java.net.URLEncoder.encode(String.join(", ", selectedSeats), "UTF-8");
            
            // Option 1: Use local file (works without server)
            // String paymentUrl = String.format("file:///%s/payment/index.html?...", ...);
            
            // Option 2: Use localhost server (better for production)
            String paymentUrl = String.format(
                "http://localhost:8080/index.html?ref=%s&concert=%s&artist=%s&date=%s&venue=%s&zone=%s&seats=%s&qty=%d&amount=%.2f",
                bookingReference,
                concertName,
                artist,
                date,
                venue,
                selectedZone,
                seatList,
                selectedSeats.size(),
                totalPrice
            );
            
            // Set pending status
            PaymentMockDB.setPaymentStatus(bookingReference, "PENDING");
            
            // Open in default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new java.net.URI(paymentUrl));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to open payment page: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkPaymentStatus() {
        // Check localStorage via browser (using Java Desktop integration)
        // For now, check our mock database
        String status = PaymentMockDB.getPaymentStatus(bookingReference);
        
        if ("PAID".equals(status)) {
            // Complete booking
            int concertId = concert.getInt("id");
            String seatNumbers = String.join(", ", selectedSeats);
            
            JSONObject result = SupabaseConfig.createBooking(userId, concertId, seatNumbers, selectedSeats.size(), totalPrice);
            
            if (result.has("error") && result.getBoolean("error")) {
                JOptionPane.showMessageDialog(this, "Booking failed: " + result.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Payment successful!\n\nBooking Reference: " + bookingReference + "\nSeats: " + seatNumbers, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                new ConcertList(userId, username);
                dispose();
            }
        } else if ("PENDING".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "Payment is still pending.\nPlease complete the payment and try again.", 
                "Payment Pending", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No payment found.\nPlease scan QR code or click the payment link.", 
                "Payment Not Found", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}

// Mock Payment Database
class PaymentMockDB {
    private static Map<String, String> payments = new HashMap<>();
    
    public static void setPaymentStatus(String reference, String status) {
        payments.put(reference, status);
    }
    
    public static String getPaymentStatus(String reference) {
        return payments.getOrDefault(reference, "NOT_FOUND");
    }
}

// Mock Payment Web Page
class MockPaymentWebPage extends JFrame {
    private String bookingReference;
    private double amount;
    private JFrame parentFrame;
    
    public MockPaymentWebPage(String bookingReference, double amount, JFrame parentFrame) {
        this.bookingReference = bookingReference;
        this.amount = amount;
        this.parentFrame = parentFrame;
        
        setTitle("Payment Gateway - Mock Bank");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(parentFrame);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);
        
        // Bank Logo
        JLabel bankLabel = new JLabel("🏦 MOCK BANK");
        bankLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        bankLabel.setForeground(new Color(0, 100, 200));
        bankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(bankLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        JLabel secureLabel = new JLabel("Secure Payment Gateway");
        secureLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        secureLabel.setForeground(Color.GRAY);
        secureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(secureLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Payment Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(245, 245, 245));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setMaximumSize(new Dimension(400, 200));
        
        JLabel merchantLabel = new JLabel("Merchant: ALL CONCERT");
        merchantLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        merchantLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(merchantLabel);
        detailsPanel.add(Box.createVerticalStrut(15));
        
        JLabel refLabel = new JLabel("Reference: " + bookingReference);
        refLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        refLabel.setForeground(Color.DARK_GRAY);
        refLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(refLabel);
        detailsPanel.add(Box.createVerticalStrut(20));
        
        JLabel amountLabel = new JLabel(String.format("Amount: ฿%.2f", amount));
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        amountLabel.setForeground(new Color(0, 150, 0));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(amountLabel);
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Payment Method (Mock)
        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        methodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(methodLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        methodPanel.setBackground(Color.WHITE);
        methodPanel.setMaximumSize(new Dimension(400, 50));
        
        String[] methods = {"💳 Credit Card", "🏦 Bank Transfer", "📱 Mobile Banking"};
        ButtonGroup group = new ButtonGroup();
        for (String method : methods) {
            JRadioButton radio = new JRadioButton(method);
            radio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            radio.setBackground(Color.WHITE);
            if (method.equals(methods[0])) radio.setSelected(true);
            group.add(radio);
            methodPanel.add(radio);
        }
        mainPanel.add(methodPanel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Pay Button
        JButton payBtn = new JButton("PAY NOW");
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        payBtn.setPreferredSize(new Dimension(300, 55));
        payBtn.setMaximumSize(new Dimension(300, 55));
        payBtn.setBackground(new Color(0, 150, 100));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);
        payBtn.setBorderPainted(false);
        payBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        payBtn.addActionListener(e -> processPayment());
        mainPanel.add(payBtn);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelBtn.setPreferredSize(new Dimension(300, 40));
        cancelBtn.setMaximumSize(new Dimension(300, 40));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(Color.GRAY);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelBtn.addActionListener(e -> {
            PaymentMockDB.setPaymentStatus(bookingReference, "CANCELLED");
            dispose();
        });
        mainPanel.add(cancelBtn);
        
        // Set pending status
        PaymentMockDB.setPaymentStatus(bookingReference, "PENDING");
        
        setVisible(true);
    }
    
    private void processPayment() {
        // Simulate payment processing
        JButton payBtn = (JButton) ((JPanel) getContentPane().getComponent(0)).getComponent(13);
        payBtn.setEnabled(false);
        payBtn.setText("Processing...");
        
        javax.swing.Timer timer = new javax.swing.Timer(2000, e -> {
            PaymentMockDB.setPaymentStatus(bookingReference, "PAID");
            JOptionPane.showMessageDialog(this, 
                "Payment Successful!\n\nPlease return to the booking page\nand click 'Check Payment Status'", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
