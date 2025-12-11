import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import org.json.JSONObject;

public class SeatSelection extends JFrame {
    private int userId;
    private String username;
    private JSONObject concert;
    private int concertId;
    private Zone currentZone;
    private SeatManager seatManager;
    private Set<String> bookedSeats = new HashSet<>();
    private JPanel seatsPanel;
    private JLabel zoneLabel;
    private JLabel qtyLabel;
    private JLabel priceLabel;
    
    public SeatSelection(int userId, String username, JSONObject concert) {
        this.userId = userId;
        this.username = username;
        this.concert = concert;
        this.concertId = concert.getInt("id");
        
        // Initialize with default zone
        this.currentZone = Zone.getZoneByName("BLUE B");
        this.seatManager = new SeatManager(currentZone);
        
        // Load booked seats from database
        loadBookedSeats();
        
        setTitle("Select Seat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(100, 20, 20));
        add(mainPanel);
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content Area
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(new Color(100, 20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Left: Zone Map
        contentPanel.add(createZoneMap(), BorderLayout.WEST);
        
        // Right: Seat Grid
        contentPanel.add(createSeatPanel(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Bottom: Confirm Button
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(120, 25, 25));
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Logo + Back
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
        
        JLabel titleLabel = new JLabel("← SELECT SEAT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new ConcertDetail(userId, username, concert);
                dispose();
            }
        });
        leftPanel.add(titleLabel);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        
        return topBar;
    }
    
    private JPanel createZoneMap() {
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setBackground(new Color(160, 140, 140));
        mapPanel.setPreferredSize(new Dimension(380, 500));
        mapPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel mainZonePanel = new JPanel();
        mainZonePanel.setLayout(new BoxLayout(mainZonePanel, BoxLayout.Y_AXIS));
        mainZonePanel.setOpaque(false);
        
        // Stage at top
        JPanel stagePanel = new JPanel();
        stagePanel.setBackground(Color.WHITE);
        stagePanel.setPreferredSize(new Dimension(200, 50));
        stagePanel.setMaximumSize(new Dimension(200, 50));
        JLabel stageLabel = new JLabel("STAGE");
        stageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        stagePanel.add(stageLabel);
        stagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(stagePanel);
        mainZonePanel.add(Box.createVerticalStrut(15));
        
        // Top row: PURPLE A, GREEN A, BLUE A
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        topRow.setOpaque(false);
        topRow.setMaximumSize(new Dimension(400, 80));
        topRow.add(createZoneButton(new Zone("PURPLE A", new Color(150, 100, 200), 6000.0, Zone.ZoneType.SEATING)));
        topRow.add(createZoneButton(new Zone("GREEN A", new Color(100, 200, 100), 4000.0, Zone.ZoneType.SEATING)));
        topRow.add(createZoneButton(new Zone("BLUE A", new Color(100, 150, 220), 3000.0, Zone.ZoneType.SEATING)));
        topRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(topRow);
        mainZonePanel.add(Box.createVerticalStrut(10));
        
        // FOH bar
        JPanel fohPanel = new JPanel();
        fohPanel.setBackground(new Color(60, 60, 60));
        fohPanel.setPreferredSize(new Dimension(300, 35));
        fohPanel.setMaximumSize(new Dimension(300, 35));
        JLabel fohLabel = new JLabel("FOH");
        fohLabel.setForeground(Color.WHITE);
        fohLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        fohPanel.add(fohLabel);
        fohPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(fohPanel);
        mainZonePanel.add(Box.createVerticalStrut(5));
        
        // Yellow bar
        JPanel yellowPanel = new JPanel();
        yellowPanel.setBackground(new Color(230, 230, 100));
        yellowPanel.setPreferredSize(new Dimension(300, 45));
        yellowPanel.setMaximumSize(new Dimension(300, 45));
        JLabel yellowLabel = new JLabel("Yellow");
        yellowLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        yellowPanel.add(yellowLabel);
        yellowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(yellowPanel);
        mainZonePanel.add(Box.createVerticalStrut(5));
        
        // Pink bar
        JPanel pinkPanel = new JPanel();
        pinkPanel.setBackground(new Color(230, 150, 180));
        pinkPanel.setPreferredSize(new Dimension(300, 45));
        pinkPanel.setMaximumSize(new Dimension(300, 45));
        JLabel pinkLabel = new JLabel("PINK");
        pinkLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        pinkPanel.add(pinkLabel);
        pinkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(pinkPanel);
        mainZonePanel.add(Box.createVerticalStrut(15));
        
        // Bottom row: BLUE B, GREEN B, PURPLE B
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(400, 80));
        bottomRow.add(createZoneButton(new Zone("BLUE B", new Color(100, 150, 220), 3000.0, Zone.ZoneType.SEATING)));
        bottomRow.add(createZoneButton(new Zone("GREEN B", new Color(100, 200, 100), 4000.0, Zone.ZoneType.SEATING)));
        bottomRow.add(createZoneButton(new Zone("PURPLE B", new Color(150, 100, 200), 6000.0, Zone.ZoneType.SEATING)));
        bottomRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainZonePanel.add(bottomRow);
        
        mapPanel.add(mainZonePanel, BorderLayout.CENTER);
        
        return mapPanel;
    }
    
    private JButton createZoneButton(Zone zone) {
        JButton btn = new JButton(zone.getName());
        btn.setBackground(zone.getColor());
        btn.setPreferredSize(new Dimension(90, 70));
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.addActionListener(e -> {
            currentZone = zone;
            seatManager = new SeatManager(zone);
            seatManager.markAsBooked(bookedSeats);
            updateSeatGrid();
        });
        return btn;
    }
    
    private JPanel createSeatPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(100, 20, 20));
        
        // Zone header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        zoneLabel = new JLabel("ZONE: " + currentZone.getName());
        zoneLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        zoneLabel.setForeground(Color.WHITE);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        priceLabel = new JLabel(String.format("฿%.2f", seatManager.getTotalPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        priceLabel.setForeground(new Color(100, 255, 100));
        
        qtyLabel = new JLabel("QTY: " + seatManager.getSelectedCount());
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        qtyLabel.setForeground(Color.WHITE);
        
        rightPanel.add(priceLabel);
        rightPanel.add(qtyLabel);
        
        headerPanel.add(zoneLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Seats grid
        seatsPanel = new JPanel(new GridLayout(4, 7, 10, 10));
        seatsPanel.setBackground(new Color(100, 20, 20));
        seatsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        updateSeatGrid();
        
        JScrollPane scrollPane = new JScrollPane(seatsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(100, 20, 20));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        legendPanel.setOpaque(false);
        
        legendPanel.add(createLegendItem("Available", new Color(180, 160, 200)));
        legendPanel.add(createLegendItem("Selected", new Color(255, 100, 150)));
        
        mainPanel.add(legendPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private void updateSeatGrid() {
        seatsPanel.removeAll();
        
        for (Seat seat : seatManager.getAllSeats()) {
            JButton seatBtn = createSeatButton(seat);
            seatsPanel.add(seatBtn);
        }
        
        // Update header labels
        zoneLabel.setText("ZONE: " + currentZone.getName());
        qtyLabel.setText("QTY: " + seatManager.getSelectedCount());
        priceLabel.setText(String.format("฿%.2f", seatManager.getTotalPrice()));
        
        seatsPanel.revalidate();
        seatsPanel.repaint();
    }
    
    private JButton createSeatButton(Seat seat) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(70, 60));
        btn.setFocusPainted(false);
        
        // Seat icon
        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
        seatPanel.setOpaque(false);
        
        String icon = seat.isBooked() ? "🔒" : "💺";
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel(seat.getDisplayId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        seatPanel.add(iconLabel);
        seatPanel.add(idLabel);
        btn.add(seatPanel);
        
        // Set colors based on state
        if (seat.isBooked()) {
            btn.setBackground(new Color(80, 80, 80));
            idLabel.setForeground(Color.GRAY);
            btn.setEnabled(false);
        } else if (seat.isSelected()) {
            btn.setBackground(new Color(255, 100, 150));
            idLabel.setForeground(Color.WHITE);
        } else {
            btn.setBackground(currentZone.getColor());
            idLabel.setForeground(Color.BLACK);
        }
        
        btn.addActionListener(e -> {
            if (seatManager.toggleSeat(seat.getId())) {
                updateSeatGrid();
            }
        });
        
        return btn;
    }
    
    private JPanel createLegendItem(String label, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(30, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel textLabel = new JLabel(label);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        panel.add(colorBox);
        panel.add(textLabel);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(new Color(100, 20, 20));
        
        JButton confirmBtn = new JButton("Confirm Booking");
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        confirmBtn.setPreferredSize(new Dimension(200, 50));
        confirmBtn.setBackground(Color.WHITE);
        confirmBtn.setForeground(new Color(100, 20, 20));
        confirmBtn.setFocusPainted(false);
        
        confirmBtn.addActionListener(e -> {
            if (seatManager.getSelectedCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            java.util.List<String> selectedSeats = seatManager.getSelectedSeatIds();
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm booking " + seatManager.getSelectedCount() + " seat(s) in " + currentZone.getName() + "?\nSeats: " + selectedSeats,
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                // Create booking directly
                int concertId = concert.getInt("id");
                String seatNumbers = String.join(", ", selectedSeats);
                double totalPrice = seatManager.getTotalPrice();
                
                JSONObject result = SupabaseConfig.createBooking(userId, concertId, seatNumbers, seatManager.getSelectedCount(), totalPrice);
                
                if (result.has("error") && result.getBoolean("error")) {
                    JOptionPane.showMessageDialog(this, "Booking failed: " + result.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Booking successful!\n\nSeats: " + seatNumbers + "\nTotal: ฿" + String.format("%.2f", totalPrice), 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    new ConcertList(userId, username);
                    dispose();
                }
            }
        });
        
        panel.add(confirmBtn);
        
        return panel;
    }
    
    private void loadBookedSeats() {
        try {
            // Get all bookings for this concert
            org.json.JSONArray bookings = SupabaseConfig.getAllBookings();
            
            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);
                
                // Check if this booking is for the current concert
                if (booking.getInt("concert_id") == concertId) {
                    String seatNumbers = booking.getString("seat_numbers");
                    
                    // Parse seat numbers (e.g., "BLUEB-A1, BLUEB-A2")
                    String[] seats = seatNumbers.split(",\\s*");
                    for (String seat : seats) {
                        bookedSeats.add(seat.trim());
                    }
                }
            }
            
            System.out.println("Loaded " + bookedSeats.size() + " booked seats for concert " + concertId);
        } catch (Exception e) {
            System.err.println("Error loading booked seats: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
