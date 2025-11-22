import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class AdminDashboard extends JFrame {
    private int userId;
    private String username;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public AdminDashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;
        
        setTitle("Admin Dashboard - All Concert");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(80, 0, 0));
        add(mainPanel);
        
        // Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        
        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(80, 0, 0));
        
        contentPanel.add(new ConcertManagementPanel(), "concerts");
        contentPanel.add(new BookingManagementPanel(), "bookings");
        contentPanel.add(new StatisticsPanel(), "statistics");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(40, 0, 0));
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        // Logo/Title
        JLabel titleLabel = new JLabel("ADMIN PANEL");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel adminLabel = new JLabel("👤 " + username);
        adminLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        adminLabel.setForeground(new Color(200, 200, 200));
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(adminLabel);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Menu Buttons
        sidebar.add(createMenuButton("🎵 Concerts", "concerts"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("🎫 Bookings", "bookings"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("📊 Statistics", "statistics"));
        sidebar.add(Box.createVerticalStrut(40));
        
        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(150, 0, 0));
        logoutBtn.setMaximumSize(new Dimension(220, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Logout from Admin Panel?", 
                "Confirm", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login();
                dispose();
            }
        });
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 0, 0));
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        button.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 0, 0));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 0, 0));
            }
        });
        
        return button;
    }
    
    // ===== CONCERT MANAGEMENT PANEL =====
    class ConcertManagementPanel extends JPanel {
        private JTable concertTable;
        private DefaultTableModel tableModel;
        
        public ConcertManagementPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(80, 0, 0));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // Top Panel
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Concert Management");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
            titleLabel.setForeground(Color.WHITE);
            
            JButton addButton = new JButton("+ Add Concert");
            addButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            addButton.setForeground(Color.WHITE);
            addButton.setBackground(new Color(0, 150, 0));
            addButton.setFocusPainted(false);
            addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addButton.addActionListener(e -> showAddConcertDialog());
            
            topPanel.add(titleLabel, BorderLayout.WEST);
            topPanel.add(addButton, BorderLayout.EAST);
            
            add(topPanel, BorderLayout.NORTH);
            
            // Table
            String[] columns = {"ID", "Name", "Artist", "Date", "Venue", "Price", "Available Seats", "Actions"};
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 7; // Only Actions column
                }
            };
            
            concertTable = new JTable(tableModel);
            concertTable.setRowHeight(40);
            concertTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
            concertTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            concertTable.setSelectionBackground(new Color(100, 0, 0));
            
            // Actions column with buttons
            concertTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            concertTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
            
            JScrollPane scrollPane = new JScrollPane(concertTable);
            add(scrollPane, BorderLayout.CENTER);
            
            loadConcerts();
        }
        
        private void loadConcerts() {
            tableModel.setRowCount(0);
            JSONArray concerts = SupabaseConfig.getAllConcerts();
            
            for (int i = 0; i < concerts.length(); i++) {
                JSONObject concert = concerts.getJSONObject(i);
                Object[] row = {
                    concert.getInt("id"),
                    concert.getString("name"),
                    concert.getString("artist"),
                    concert.getString("date"),
                    concert.getString("venue"),
                    String.format("฿%.2f", concert.getDouble("price")),
                    concert.getInt("seats_available"),
                    "Edit | Delete"
                };
                tableModel.addRow(row);
            }
        }
        
        private void showAddConcertDialog() {
            JDialog dialog = new JDialog(AdminDashboard.this, "Add New Concert", true);
            dialog.setSize(700, 750);
            dialog.setLocationRelativeTo(AdminDashboard.this);
            
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
            
            JTextField nameField = new JTextField();
            JTextField artistField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField venueField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField seatsField = new JTextField();
            JTextField imageUrlField = new JTextField();
            JTextArea descArea = new JTextArea(3, 20);
            descArea.setLineWrap(true);
            
            // Image preview
            JLabel imagePreview = new JLabel();
            imagePreview.setPreferredSize(new Dimension(200, 200));
            imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            imagePreview.setHorizontalAlignment(JLabel.CENTER);
            imagePreview.setText("No image selected");
            
            // Browse button for file selection
            JButton browseBtn = new JButton("Browse...");
            browseBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(dialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    imageUrlField.setText(path);
                    
                    // Show preview
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(img));
                    imagePreview.setText("");
                }
            });
            
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            imagePanel.add(imageUrlField);
            imagePanel.add(browseBtn);
            
            formPanel.add(new JLabel("Concert Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Artist:"));
            formPanel.add(artistField);
            formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
            formPanel.add(dateField);
            formPanel.add(new JLabel("Venue:"));
            formPanel.add(venueField);
            formPanel.add(new JLabel("Price (฿):"));
            formPanel.add(priceField);
            formPanel.add(new JLabel("Total Seats:"));
            formPanel.add(seatsField);
            formPanel.add(new JLabel("Image Path/URL:"));
            formPanel.add(imagePanel);
            formPanel.add(new JLabel("Description:"));
            formPanel.add(new JScrollPane(descArea));
            
            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");
            
            saveBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    String artist = artistField.getText().trim();
                    String date = dateField.getText().trim();
                    String venue = venueField.getText().trim();
                    String imageUrl = imageUrlField.getText().trim();
                    double price = Double.parseDouble(priceField.getText());
                    int seats = Integer.parseInt(seatsField.getText());
                    String desc = descArea.getText().trim();
                    
                    if (name.isEmpty() || artist.isEmpty() || date.isEmpty() || venue.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    JSONObject result = SupabaseConfig.addConcert(name, artist, date, venue, price, seats, desc, imageUrl);
                    
                    if (result.has("error")) {
                        JOptionPane.showMessageDialog(dialog, "Failed to add concert: " + result.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Concert added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadConcerts();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and seats!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelBtn.addActionListener(e -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(imagePreview, BorderLayout.EAST);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.add(mainPanel);
            dialog.setVisible(true);
        }
        
        private void showEditConcertDialog(int concertId) {
            // Get concert details
            JSONObject concert = SupabaseConfig.getConcertById(concertId);
            if (concert == null || concert.length() == 0) {
                JOptionPane.showMessageDialog(this, "Concert not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog(AdminDashboard.this, "Edit Concert", true);
            dialog.setSize(700, 750);
            dialog.setLocationRelativeTo(AdminDashboard.this);
            
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
            
            JTextField nameField = new JTextField(concert.optString("name", ""));
            JTextField artistField = new JTextField(concert.optString("artist", ""));
            JTextField dateField = new JTextField(concert.optString("date", ""));
            JTextField venueField = new JTextField(concert.optString("venue", ""));
            JTextField priceField = new JTextField(String.valueOf(concert.optDouble("price", 0)));
            JTextField seatsField = new JTextField(String.valueOf(concert.optInt("seats_total", 0)));
            JTextField imageUrlField = new JTextField(concert.optString("image_url", ""));
            JTextArea descArea = new JTextArea(concert.optString("description", ""), 3, 20);
            descArea.setLineWrap(true);
            
            // Image preview
            JLabel imagePreview = new JLabel();
            imagePreview.setPreferredSize(new Dimension(200, 200));
            imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            imagePreview.setHorizontalAlignment(JLabel.CENTER);
            
            // Load existing image if available
            String existingImage = concert.optString("image_url", "");
            if (!existingImage.isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(existingImage);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    imagePreview.setText("Image not found");
                }
            } else {
                imagePreview.setText("No image");
            }
            
            // Browse button for file selection
            JButton browseBtn = new JButton("Browse...");
            browseBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(dialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    imageUrlField.setText(path);
                    
                    // Show preview
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(img));
                    imagePreview.setText("");
                }
            });
            
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            imagePanel.add(imageUrlField);
            imagePanel.add(browseBtn);
            
            formPanel.add(new JLabel("Concert Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Artist:"));
            formPanel.add(artistField);
            formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
            formPanel.add(dateField);
            formPanel.add(new JLabel("Venue:"));
            formPanel.add(venueField);
            formPanel.add(new JLabel("Price (฿):"));
            formPanel.add(priceField);
            formPanel.add(new JLabel("Total Seats:"));
            formPanel.add(seatsField);
            formPanel.add(new JLabel("Image Path/URL:"));
            formPanel.add(imagePanel);
            formPanel.add(new JLabel("Description:"));
            formPanel.add(new JScrollPane(descArea));
            
            JButton saveBtn = new JButton("Update");
            JButton cancelBtn = new JButton("Cancel");
            
            saveBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    String artist = artistField.getText().trim();
                    String date = dateField.getText().trim();
                    String venue = venueField.getText().trim();
                    String imageUrl = imageUrlField.getText().trim();
                    double price = Double.parseDouble(priceField.getText());
                    int seats = Integer.parseInt(seatsField.getText());
                    String desc = descArea.getText().trim();
                    
                    if (name.isEmpty() || artist.isEmpty() || date.isEmpty() || venue.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    JSONObject result = SupabaseConfig.updateConcert(concertId, name, artist, date, venue, price, seats, desc, imageUrl);
                    
                    if (result.has("error")) {
                        JOptionPane.showMessageDialog(dialog, "Failed to update concert: " + result.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Concert updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadConcerts();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and seats!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelBtn.addActionListener(e -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(imagePreview, BorderLayout.EAST);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.add(mainPanel);
            dialog.setVisible(true);
        }
        
        // Button Renderer
        class ButtonRenderer extends JPanel implements TableCellRenderer {
            private JButton editBtn = new JButton("Edit");
            private JButton deleteBtn = new JButton("Delete");
            
            public ButtonRenderer() {
                setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
                editBtn.setBackground(new Color(0, 100, 200));
                editBtn.setForeground(Color.WHITE);
                deleteBtn.setBackground(new Color(200, 0, 0));
                deleteBtn.setForeground(Color.WHITE);
                add(editBtn);
                add(deleteBtn);
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                return this;
            }
        }
        
        // Button Editor
        class ButtonEditor extends DefaultCellEditor {
            private JPanel panel;
            private JButton editBtn;
            private JButton deleteBtn;
            private int currentRow;
            
            public ButtonEditor(JCheckBox checkBox) {
                super(checkBox);
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                editBtn = new JButton("Edit");
                deleteBtn = new JButton("Delete");
                
                editBtn.setBackground(new Color(0, 100, 200));
                editBtn.setForeground(Color.WHITE);
                deleteBtn.setBackground(new Color(200, 0, 0));
                deleteBtn.setForeground(Color.WHITE);
                
                editBtn.addActionListener(e -> {
                    int id = (Integer) tableModel.getValueAt(currentRow, 0);
                    showEditConcertDialog(id);
                    fireEditingStopped();
                });
                
                deleteBtn.addActionListener(e -> {
                    int id = (Integer) tableModel.getValueAt(currentRow, 0);
                    String name = (String) tableModel.getValueAt(currentRow, 1);
                    int confirm = JOptionPane.showConfirmDialog(panel, 
                        "Delete concert \"" + name + "\"?", 
                        "Confirm Delete", 
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = SupabaseConfig.deleteConcert(id);
                        if (success) {
                            JOptionPane.showMessageDialog(panel, "Concert deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadConcerts();
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to delete concert!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    fireEditingStopped();
                });
                
                panel.add(editBtn);
                panel.add(deleteBtn);
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row;
                return panel;
            }
        }
    }
    
    // ===== BOOKING MANAGEMENT PANEL =====
    class BookingManagementPanel extends JPanel {
        private JTable bookingTable;
        private DefaultTableModel tableModel;
        
        public BookingManagementPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(80, 0, 0));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Booking Management");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
            
            add(titleLabel, BorderLayout.NORTH);
            
            // Table
            String[] columns = {"ID", "Username", "Concert", "Seats", "Quantity", "Total", "Date", "Status"};
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            bookingTable = new JTable(tableModel);
            bookingTable.setRowHeight(35);
            bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
            bookingTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            add(scrollPane, BorderLayout.CENTER);
            
            loadBookings();
        }
        
        private void loadBookings() {
            tableModel.setRowCount(0);
            JSONArray bookings = SupabaseConfig.getAllBookings();
            
            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);
                
                // Get username and concert name from nested objects
                String username = "N/A";
                String concertName = "N/A";
                
                if (booking.has("users") && !booking.isNull("users")) {
                    JSONObject user = booking.getJSONObject("users");
                    username = user.optString("username", "N/A");
                }
                
                if (booking.has("concerts") && !booking.isNull("concerts")) {
                    JSONObject concert = booking.getJSONObject("concerts");
                    concertName = concert.optString("name", "N/A");
                }
                
                Object[] row = {
                    booking.getInt("id"),
                    username,
                    concertName,
                    booking.optString("seat_numbers", "N/A"),
                    booking.getInt("quantity"),
                    String.format("฿%.2f", booking.getDouble("total_price")),
                    booking.getString("booking_date"),
                    booking.getString("status")
                };
                tableModel.addRow(row);
            }
        }
    }
    
    // ===== STATISTICS PANEL =====
    class StatisticsPanel extends JPanel {
        public StatisticsPanel() {
            setLayout(new GridLayout(2, 2, 20, 20));
            setBackground(new Color(80, 0, 0));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // Get statistics
            JSONObject stats = SupabaseConfig.getStatistics();
            
            add(createStatCard("Total Concerts", String.valueOf(stats.optInt("totalConcerts", 0)), "🎵"));
            add(createStatCard("Total Bookings", String.valueOf(stats.optInt("totalBookings", 0)), "🎫"));
            add(createStatCard("Total Revenue", String.format("฿%.2f", stats.optDouble("totalRevenue", 0)), "💰"));
            add(createStatCard("Active Users", String.valueOf(stats.optInt("activeUsers", 0)), "👥"));
        }
        
        private JPanel createStatCard(String title, String value, String icon) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(new Color(60, 0, 0));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 0, 0), 2),
                new EmptyBorder(30, 30, 30, 30)
            ));
            
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 60));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
            titleLabel.setForeground(new Color(200, 200, 200));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            card.add(iconLabel);
            card.add(Box.createVerticalStrut(20));
            card.add(titleLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(valueLabel);
            
            return card;
        }
    }
}
