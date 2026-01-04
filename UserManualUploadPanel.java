import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * User Manual Upload Panel for Admin
 * อนุญาตให้แอดมินอัพโหลด User Manual (PDF, MD, TXT)
 */
public class UserManualUploadPanel extends JPanel {
    private static final String MANUAL_DIRECTORY = "manuals/";
    private JTextArea logArea;
    private JLabel currentManualLabel;
    private File currentManualFile;
    
    public UserManualUploadPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(100, 20, 20));
        
        // สร้างโฟลเดอร์ manuals ถ้ายังไม่มี
        createManualDirectory();
        
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("📖 User Manual Upload");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Center Content
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        
        // Left: Upload Section
        centerPanel.add(createUploadSection());
        
        // Right: Preview Section
        centerPanel.add(createPreviewSection());
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Load current manual
        loadCurrentManual();
    }
    
    /**
     * สร้างโฟลเดอร์สำหรับเก็บ manual
     */
    private void createManualDirectory() {
        try {
            Files.createDirectories(Paths.get(MANUAL_DIRECTORY));
        } catch (IOException e) {
            logMessage("⚠️ Warning: Could not create manuals directory");
        }
    }
    
    /**
     * สร้าง Upload Section
     */
    private JPanel createUploadSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(80, 15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 50, 50), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Section Title
        JLabel sectionTitle = new JLabel("Upload New Manual");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sectionTitle);
        panel.add(Box.createVerticalStrut(20));
        
        // Instructions
        JTextArea instructions = new JTextArea(
            "Instructions:\n" +
            "• Supported files: PDF, MD, TXT\n" +
            "• Maximum file size: 10 MB\n" +
            "• Uploaded file will replace old manual\n" +
            "• Users can download immediately"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(60, 10, 10));
        instructions.setForeground(new Color(180, 180, 180));
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instructions.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(instructions);
        panel.add(Box.createVerticalStrut(20));
        
        // Upload Buttons
        JButton uploadPDFBtn = createButton("📄 Upload PDF Manual", new Color(200, 80, 80));
        uploadPDFBtn.addActionListener(e -> uploadManual("pdf"));
        uploadPDFBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(uploadPDFBtn);
        panel.add(Box.createVerticalStrut(10));
        
        JButton uploadMDBtn = createButton("📝 Upload Markdown (.md)", new Color(80, 120, 200));
        uploadMDBtn.addActionListener(e -> uploadManual("md"));
        uploadMDBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(uploadMDBtn);
        panel.add(Box.createVerticalStrut(10));
        
        JButton uploadTXTBtn = createButton("📋 Upload Text File (.txt)", new Color(80, 150, 120));
        uploadTXTBtn.addActionListener(e -> uploadManual("txt"));
        uploadTXTBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(uploadTXTBtn);
        panel.add(Box.createVerticalStrut(25));
        
        // Delete Button
        JButton deleteBtn = createButton("🗑️ Delete Current Manual", new Color(150, 50, 50));
        deleteBtn.addActionListener(e -> deleteCurrentManual());
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(deleteBtn);
        panel.add(Box.createVerticalStrut(20));
        
        // Log Area
        JLabel logLabel = new JLabel("Upload Log:");
        logLabel.setForeground(Color.WHITE);
        logLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(logLabel);
        panel.add(Box.createVerticalStrut(5));
        
        logArea = new JTextArea(8, 30);
        logArea.setEditable(false);
        logArea.setBackground(new Color(40, 5, 5));
        logArea.setForeground(new Color(100, 200, 150));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(logScroll);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * สร้าง Preview Section
     */
    private JPanel createPreviewSection() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(80, 15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 50, 50), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Title
        JLabel previewTitle = new JLabel("Current Manual");
        previewTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        previewTitle.setForeground(Color.WHITE);
        panel.add(previewTitle, BorderLayout.NORTH);
        
        // Manual Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(60, 10, 10));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        currentManualLabel = new JLabel("No manual uploaded yet");
        currentManualLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        currentManualLabel.setForeground(new Color(200, 200, 200));
        currentManualLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(currentManualLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        
        // Preview or Download Button
        JButton viewBtn = new JButton("👁️ View Manual");
        viewBtn.setBackground(new Color(60, 120, 150));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.addActionListener(e -> viewCurrentManual());
        infoPanel.add(viewBtn);
        infoPanel.add(Box.createVerticalStrut(10));
        
        JButton openFolderBtn = new JButton("📁 Open Manuals Folder");
        openFolderBtn.setBackground(new Color(80, 100, 150));
        openFolderBtn.setForeground(Color.WHITE);
        openFolderBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        openFolderBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        openFolderBtn.addActionListener(e -> openManualsFolder());
        infoPanel.add(openFolderBtn);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Quick Stats
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statsPanel.setBackground(new Color(60, 10, 10));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        File manualDir = new File(MANUAL_DIRECTORY);
        File[] files = manualDir.listFiles();
        int fileCount = (files != null) ? files.length : 0;
        
        addStatLabel(statsPanel, "📊 Total Manuals: " + fileCount);
        addStatLabel(statsPanel, "📂 Location: " + MANUAL_DIRECTORY);
        addStatLabel(statsPanel, "✅ Status: " + (fileCount > 0 ? "Active" : "No Manual"));
        
        panel.add(statsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * เพิ่ม stat label
     */
    private void addStatLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(180, 180, 180));
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panel.add(label);
    }
    
    /**
     * อัพโหลด manual
     */
    private void uploadManual(String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select " + fileType.toUpperCase() + " File");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            fileType.toUpperCase() + " Files", fileType
        );
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // ตรวจสอบขนาดไฟล์ (10 MB)
            if (selectedFile.length() > 10 * 1024 * 1024) {
                JOptionPane.showMessageDialog(this,
                    "ไฟล์ใหญ่เกินไป! ขนาดสูงสุด 10 MB",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                logMessage("❌ Upload failed: File too large");
                return;
            }
            
            try {
                // คัดลอกไฟล์ไปยังโฟลเดอร์ manuals
                String newFileName = "USER_MANUAL." + fileType;
                Path destination = Paths.get(MANUAL_DIRECTORY, newFileName);
                
                Files.copy(selectedFile.toPath(), destination, 
                          StandardCopyOption.REPLACE_EXISTING);
                
                logMessage("✅ Successfully uploaded: " + newFileName);
                logMessage("📁 Size: " + formatFileSize(selectedFile.length()));
                
                JOptionPane.showMessageDialog(this,
                    "User Manual uploaded successfully!\n\n" +
                    "File: " + newFileName + "\n" +
                    "Location: " + MANUAL_DIRECTORY,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload current manual
                loadCurrentManual();
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Upload failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                logMessage("❌ Upload failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * ลบ manual ปัจจุบัน
     */
    private void deleteCurrentManual() {
        if (currentManualFile == null || !currentManualFile.exists()) {
            JOptionPane.showMessageDialog(this,
                "No manual to delete",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete current User Manual?\n\n" + currentManualFile.getName(),
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Files.delete(currentManualFile.toPath());
                logMessage("✅ Manual deleted: " + currentManualFile.getName());
                currentManualFile = null;
                loadCurrentManual();
                
                JOptionPane.showMessageDialog(this,
                    "User Manual deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                logMessage("❌ Delete failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * โหลด manual ปัจจุบัน
     */
    private void loadCurrentManual() {
        File manualDir = new File(MANUAL_DIRECTORY);
        File[] files = manualDir.listFiles((dir, name) -> 
            name.startsWith("USER_MANUAL") && 
            (name.endsWith(".pdf") || name.endsWith(".md") || name.endsWith(".txt"))
        );
        
        if (files != null && files.length > 0) {
            currentManualFile = files[0];
            String info = String.format(
                "<html>📄 <b>%s</b><br>" +
                "📊 Size: %s<br>" +
                "📅 Modified: %s</html>",
                currentManualFile.getName(),
                formatFileSize(currentManualFile.length()),
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(currentManualFile.lastModified())
            );
            currentManualLabel.setText(info);
            logMessage("📖 Current manual: " + currentManualFile.getName());
        } else {
            currentManualFile = null;
            currentManualLabel.setText("<html>❌ No manual uploaded<br><br>" +
                "Please upload a User Manual file</html>");
            logMessage("ℹ️ No manual found");
        }
    }
    
    /**
     * ดู manual ปัจจุบัน
     */
    private void viewCurrentManual() {
        if (currentManualFile == null || !currentManualFile.exists()) {
            JOptionPane.showMessageDialog(this,
                "No manual available to view",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Desktop.getDesktop().open(currentManualFile);
            logMessage("👁️ Opening: " + currentManualFile.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Could not open file: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            logMessage("❌ Could not open file");
        }
    }
    
    /**
     * เปิดโฟลเดอร์ manuals
     */
    private void openManualsFolder() {
        try {
            Desktop.getDesktop().open(new File(MANUAL_DIRECTORY));
            logMessage("📁 Opening manuals folder");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Could not open folder: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Format file size
     */
    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }
    
    /**
     * เพิ่มข้อความใน log
     */
    private void logMessage(String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss")
            .format(new java.util.Date());
        logArea.append("[" + timestamp + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    /**
     * สร้างปุ่ม
     */
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(250, 40));
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
}
