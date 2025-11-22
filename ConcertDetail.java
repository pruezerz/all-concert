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
    private JSpinner quantitySpinner;
    
    public ConcertDetail(int userId, String username, JSONObject concert) {
        this.userId = userId;
        this.username = username;
        this.concert = concert;
        
        setTitle("Concert Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(80, 0, 0));
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
        topBar.setBackground(new Color(80, 0, 0));
        topBar.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(100, 0, 0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ConcertList(userId, username);
            dispose();
        });
        
        topBar.add(backButton, BorderLayout.WEST);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(80, 0, 0));
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
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Concert Info
        String name = concert.getString("name");
        String artist = concert.getString("artist");
        String date = concert.getString("date");
        String venue = concert.getString("venue");
        double price = concert.getDouble("price");
        int seatsAvailable = concert.getInt("seats_available");
        String description = concert.optString("description", "No description available");
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel artistLabel = new JLabel("Artist: " + artist);
        artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        artistLabel.setForeground(new Color(200, 200, 200));
        artistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dateLabel = new JLabel("📅 Date: " + date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dateLabel.setForeground(new Color(200, 200, 200));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel venueLabel = new JLabel("📍 Venue: " + venue);
        venueLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        venueLabel.setForeground(new Color(200, 200, 200));
        venueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel seatsLabel = new JLabel("🎫 Available Seats: " + seatsAvailable);
        seatsLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        seatsLabel.setForeground(new Color(255, 200, 0));
        seatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel(String.format("💰 Price: ฿%.2f per ticket", price));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        priceLabel.setForeground(new Color(255, 200, 0));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(artistLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(dateLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(venueLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(seatsLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        descLabel.setForeground(Color.WHITE);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descArea.setForeground(new Color(200, 200, 200));
        descArea.setBackground(new Color(60, 0, 0));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descArea);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Booking Panel
        contentPanel.add(createBookingPanel(price, seatsAvailable));
        
        return contentPanel;
    }
    
    private JPanel createBookingPanel(double price, int maxSeats) {
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        bookingPanel.setBackground(new Color(60, 0, 0));
        bookingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        bookingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel bookingTitle = new JLabel("Book Tickets");
        bookingTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        bookingTitle.setForeground(Color.WHITE);
        bookingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.setOpaque(false);
        
        JLabel qtyLabel = new JLabel("Quantity: ");
        qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        qtyLabel.setForeground(Color.WHITE);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, Math.min(maxSeats, 10), 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(new Font("SansSerif", Font.PLAIN, 18));
        quantitySpinner.setPreferredSize(new Dimension(80, 35));
        
        JLabel totalLabel = new JLabel(String.format("Total: ฿%.2f", price));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setForeground(new Color(255, 200, 0));
        
        quantitySpinner.addChangeListener(e -> {
            int qty = (Integer) quantitySpinner.getValue();
            totalLabel.setText(String.format("Total: ฿%.2f", price * qty));
        });
        
        quantityPanel.add(qtyLabel);
        quantityPanel.add(quantitySpinner);
        quantityPanel.add(Box.createHorizontalStrut(20));
        quantityPanel.add(totalLabel);
        
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        bookButton.setForeground(new Color(80, 0, 0));
        bookButton.setBackground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.setPreferredSize(new Dimension(200, 50));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(e -> processBooking(price));
        
        bookingPanel.add(bookingTitle);
        bookingPanel.add(Box.createVerticalStrut(15));
        bookingPanel.add(quantityPanel);
        bookingPanel.add(Box.createVerticalStrut(20));
        bookingPanel.add(bookButton);
        
        return bookingPanel;
    }
    
    private void processBooking(double pricePerTicket) {
        int quantity = (Integer) quantitySpinner.getValue();
        double totalPrice = pricePerTicket * quantity;
        int concertId = concert.getInt("id");
        
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Confirm booking:\n%d ticket(s) for ฿%.2f?", quantity, totalPrice),
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String seatNumbers = generateSeatNumbers(quantity);
            
            JSONObject result = SupabaseConfig.createBooking(userId, concertId, seatNumbers, quantity, totalPrice);
            
            if (result.has("error") && result.getBoolean("error")) {
                JOptionPane.showMessageDialog(this,
                    "Booking failed: " + result.getString("message"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                // Update available seats
                int currentSeats = concert.getInt("seats_available");
                SupabaseConfig.updateConcertSeats(concertId, currentSeats - quantity);
                
                JOptionPane.showMessageDialog(this,
                    "Booking successful!\nSeats: " + seatNumbers,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                new ConcertList(userId, username);
                dispose();
            }
        }
    }
    
    private String generateSeatNumbers(int quantity) {
        StringBuilder seats = new StringBuilder();
        int startSeat = (int)(Math.random() * 100) + 1;
        for (int i = 0; i < quantity; i++) {
            if (i > 0) seats.append(", ");
            seats.append("A").append(startSeat + i);
        }
        return seats.toString();
    }
}
