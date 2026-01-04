# ภาคผนวก - All Concert Ticket Booking System

---

## 📋 1. ปริมาณงานและขอบเขตของโปรแกรม

### ขนาดโปรเจ็กต์
- **จำนวนไฟล์ทั้งหมด:** 17 ไฟล์ Java
- **บรรทัดโค้ดโดยประมาณ:** 4,000+ บรรทัด
- **จำนวน Class:** 17 classes (รวม inner classes และ enums)

### โครงสร้างโปรเจ็กต์

#### 📱 UI Layer (9 Classes)
1. **Login.java** (330 บรรทัด) - หน้าล็อกอิน
2. **Register.java** (~350 บรรทัด) - หน้าลงทะเบียน
3. **ConcertList.java** (~400 บรรทัด) - แสดงรายการคอนเสิร์ตทั้งหมด
4. **ConcertDetail.java** (~350 บรรทัด) - แสดงรายละเอียดคอนเสิร์ต
5. **SeatSelection.java** (~500 บรรทัด) - เลือกที่นั่ง/โซน
6. **PaymentPage.java** (~450 บรรทัด) - หน้าชำระเงินและยืนยัน
7. **MyBookings.java** (~400 บรรทัด) - แสดงประวัติการจอง
8. **AdminDashboard.java** (744 บรรทัด) - แผงควบคุมแอดมิน
9. **UserManualUploadPanel.java** (434 บรรทัด) - อัพโหลดคู่มือผู้ใช้

#### 🧩 Model Layer (3 Classes)
10. **Seat.java** (~80 บรรทัด) - โมเดลที่นั่ง
11. **SeatManager.java** (106 บรรทัด) - จัดการที่นั่ง
12. **Zone.java** (~70 บรรทัด) - โมเดลโซนที่นั่ง

#### 🔧 Utility Layer (3 Classes)
13. **UIUtils.java** (218 บรรทัด) - คลาสช่วยเหลือสร้าง UI
14. **SupabaseConfig.java** (~600 บรรทัด) - เชื่อมต่อฐานข้อมูล
15. **ConcertSearchSort.java** (330 บรรทัด) - ค้นหาและเรียงลำดับ

#### 📁 Other Classes (2 Classes)
16. **ImageUploader.java** (141 บรรทัด) - อัพโหลดรูปภาพ
17. **UserManualUploadPanel.java** - จัดการไฟล์คู่มือ

### ฟีเจอร์หลัก

#### สำหรับผู้ใช้ทั่วไป
- ✅ ลงทะเบียนและล็อกอิน
- ✅ ค้นหาคอนเสิร์ตด้วย keyword
- ✅ เรียงลำดับคอนเสิร์ต (วันที่, ราคา, ชื่อ)
- ✅ ดูรายละเอียดคอนเสิร์ต
- ✅ เลือกโซนและที่นั่ง
- ✅ ชำระเงินด้วย QR Code
- ✅ ดูประวัติการจอง
- ✅ ยกเลิกการจอง

#### สำหรับแอดมิน
- ✅ จัดการคอนเสิร์ต (เพิ่ม/แก้ไข/ลบ)
- ✅ จัดการการจอง
- ✅ ดูสถิติระบบ
- ✅ อัพโหลดคู่มือผู้ใช้ (PDF, MD, TXT)
- ✅ อัพโหลดรูปภาพคอนเสิร์ต

### เทคโนโลยีที่ใช้
- **Language:** Java
- **GUI Framework:** Swing
- **Database:** Supabase (PostgreSQL)
- **HTTP Client:** HttpURLConnection
- **JSON Library:** org.json
- **Image Upload:** Catbox.moe API

---

## 🧬 2. การประยุกต์ใช้แนวคิด Inheritance

### 2.1 Inheritance จาก JFrame
**ตำแหน่ง:** ทุก UI Class สืบทอดจาก `JFrame`

```java
// Login.java
public class Login extends JFrame {
    public Login() {
        setTitle("All Concert - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        // ...
    }
}

// Register.java
public class Register extends JFrame {
    public Register() {
        setTitle("All Concert - Register");
        // สืบทอด methods: setSize(), setTitle(), setVisible() จาก JFrame
    }
}

// ConcertList.java
public class ConcertList extends JFrame {
    // สืบทอดคุณสมบัติทั้งหมดของ JFrame
}
```

**คลาสที่ใช้ Inheritance จาก JFrame (9 คลาส):**
1. Login
2. Register
3. ConcertList
4. ConcertDetail
5. SeatSelection
6. PaymentPage
7. MyBookings
8. AdminDashboard
9. MockPaymentWebPage (inner class)

**ประโยชน์:**
- ✅ ใช้ methods พื้นฐานของ JFrame ได้ทันที (setTitle, setSize, setVisible, dispose)
- ✅ ไม่ต้องเขียนโค้ดซ้ำสำหรับการสร้าง window
- ✅ รองรับการปรับแต่งและ override methods ตามต้องการ

### 2.2 Inheritance จาก JPanel
**ตำแหน่ง:** `UserManualUploadPanel.java` และ Panel components อื่นๆ

```java
// UserManualUploadPanel.java
public class UserManualUploadPanel extends JPanel {
    public UserManualUploadPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(100, 20, 20));
        // สืบทอด methods ทั้งหมดจาก JPanel
    }
}

// AdminDashboard.java - Inner Class
class ButtonRenderer extends JPanel implements TableCellRenderer {
    // สืบทอด JPanel และ implement interface
}
```

**ประโยชน์:**
- ✅ สร้าง custom panel ที่มี functionality เฉพาะ
- ✅ ใช้ layout managers และการจัดการ components ของ JPanel
- ✅ ใช้ซ้ำได้ใน CardLayout (AdminDashboard)

### 2.3 Override Methods
**ตัวอย่างการ Override `paintComponent` ใน Login.java:**

```java
JPanel mainPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(80, 0, 0)); // Dark Red Background
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
};
```

**ตัวอย่างการ Override methods ใน Seat.java:**

```java
public class Seat {
    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Seat seat = (Seat) obj;
        return id.equals(seat.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
```

---

## 🔄 3. การประยุกต์ใช้แนวคิด Polymorphism

### 3.1 Method Overloading (Compile-time Polymorphism)

**ตำแหน่ง:** `Seat.java` - Constructor Overloading

```java
public class Seat {
    // Constructor 1: รับแค่ ID (ใช้ state default)
    public Seat(String id) {
        this(id, SeatState.AVAILABLE);
    }
    
    // Constructor 2: รับทั้ง ID และ state
    public Seat(String id, SeatState state) {
        this.id = id;
        this.state = state;
    }
}
```

**การใช้งาน:**
```java
// เรียกใช้แบบที่ 1
Seat seat1 = new Seat("A1");

// เรียกใช้แบบที่ 2
Seat seat2 = new Seat("A2", SeatState.BOOKED);
```

**ตำแหน่ง:** `ConcertSearchSort.java` - Method Overloading

```java
public class ConcertSearchSort {
    // Method 1: ค้นหาด้วย keyword เท่านั้น
    public static JSONArray searchConcerts(String keyword) {
        // ...
    }
    
    // Method 2: ค้นหาพร้อมกรองราคา
    public static JSONArray searchConcertsWithPrice(String keyword, 
                                                    double minPrice, 
                                                    double maxPrice) {
        JSONArray filteredConcerts = searchConcerts(keyword); // เรียก method 1
        // กรองด้วยราคาต่อ
    }
}
```

### 3.2 Interface Implementation (Runtime Polymorphism)

**ตำแหน่ง:** `AdminDashboard.java` - TableCellRenderer

```java
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton button;
    
    public ButtonRenderer(String text) {
        button = new JButton(text);
        add(button);
    }
    
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, 
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Implementation specific to rendering buttons in table
        return this;
    }
}
```

**ประโยชน์:**
- ✅ JTable สามารถใช้ ButtonRenderer เป็น renderer ได้
- ✅ ปรับแต่งการแสดงผลใน table cell แบบ custom

### 3.3 Enum Polymorphism

**ตำแหน่ง:** `ConcertSearchSort.java` - SortType Enum

```java
public enum SortType {
    DATE_ASC("Date (Earliest First)"),
    DATE_DESC("Date (Latest First)"),
    PRICE_ASC("Price (Low to High)"),
    PRICE_DESC("Price (High to Low)"),
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)");
    
    private String displayName;
    
    SortType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

**การใช้งาน:**
```java
// ใช้ enum เดียวกันในหลายบริบท
SortType sortType = SortType.DATE_ASC;
String display = sortType.getDisplayName(); // "Date (Earliest First)"

// Switch case - polymorphic behavior
switch (sortType) {
    case DATE_ASC:
        concertList.sort((a, b) -> compareByDate(a, b, true));
        break;
    case PRICE_ASC:
        concertList.sort((a, b) -> compareByPrice(a, b, true));
        break;
    // ...
}
```

### 3.4 Callback Polymorphism

**ตำแหน่ง:** `ImageUploader.java` - Progress Callback Interface

```java
public interface ProgressCallback {
    void onProgress(int percentage, String message);
}

// ใช้ polymorphism ผ่าน lambda/anonymous class
public static String uploadImageWithProgress(
        File imageFile, 
        ProgressCallback progressCallback) {
    
    progressCallback.onProgress(10, "Reading file...");
    // ...
    progressCallback.onProgress(50, "Uploading...");
    // ...
    progressCallback.onProgress(100, "Complete!");
}

// การเรียกใช้ - ส่ง implementation ที่แตกต่างกัน
uploadImageWithProgress(file, (percent, msg) -> {
    progressBar.setValue(percent);
    statusLabel.setText(msg);
});
```

---

## 🏗️ 4. การประยุกต์ใช้แนวคิด Aggregation/Composition

### 4.1 Composition (Has-A Relationship แบบแน่นแฟ้น)

#### ตัวอย่างที่ 1: SeatSelection HAS-A SeatManager
**ตำแหน่ง:** `SeatSelection.java`

```java
public class SeatSelection extends JFrame {
    private int userId;
    private String username;
    private JSONObject concert;
    private int concertId;
    private Zone currentZone;
    private SeatManager seatManager;  // Composition
    private Set<String> bookedSeats;
    
    public SeatSelection(int userId, String username, JSONObject concert) {
        this.userId = userId;
        this.username = username;
        this.concert = concert;
        this.concertId = concert.getInt("id");
        this.bookedSeats = new HashSet<>();
        
        // สร้าง SeatManager (lifecycle ผูกกับ SeatSelection)
        this.seatManager = new SeatManager(currentZone);
    }
}
```

**ลักษณะ Composition:**
- ✅ `SeatManager` ถูกสร้างโดย `SeatSelection`
- ✅ เมื่อ `SeatSelection` ถูกทำลาย, `SeatManager` ก็ถูกทำลายตามไปด้วย
- ✅ `SeatManager` ไม่สามารถมีอยู่โดยอิสระนอก `SeatSelection`

#### ตัวอย่างที่ 2: SeatManager HAS-A Collection of Seats
**ตำแหน่ง:** `SeatManager.java`

```java
public class SeatManager {
    private final Map<String, Seat> seats;  // Composition
    private final Zone currentZone;
    private double totalPrice;
    
    public SeatManager(Zone zone) {
        this.currentZone = zone;
        this.seats = new HashMap<>();  // สร้าง collection
        this.totalPrice = 0.0;
        initializeSeats();  // สร้าง Seat objects
    }
    
    private void initializeSeats() {
        if (currentZone.hasSeats()) {
            char[] rows = {'A', 'B', 'C', 'D'};
            for (char row : rows) {
                for (int num = 1; num <= 7; num++) {
                    String seatId = currentZone.getPrefix() + "-" + row + num;
                    seats.put(seatId, new Seat(seatId));  // สร้าง Seat
                }
            }
        }
    }
}
```

**ลักษณะ Composition:**
- ✅ `Seat` objects ถูกสร้างและเป็นเจ้าของโดย `SeatManager`
- ✅ เมื่อ `SeatManager` ถูกทำลาย, `Seat` objects ทั้งหมดถูกทำลายด้วย
- ✅ Lifecycle ผูกติดกันแน่นแฟ้น

#### ตัวอย่างที่ 3: MyBookings HAS-A BookingCard (Inner Class)
**ตำแหน่ง:** `MyBookings.java`

```java
public class MyBookings extends JFrame {
    private int userId;
    private String username;
    private JPanel bookingsPanel;
    
    // Inner class - Strong composition
    private class BookingCard extends JPanel {
        private JSONObject booking;
        
        public BookingCard(JSONObject booking) {
            this.booking = booking;
            setLayout(new BorderLayout(15, 15));
            // ... สร้าง UI
        }
        
        private void cancelBooking(int bookingId) {
            // เข้าถึง members ของ outer class ได้
            JSONObject result = SupabaseConfig.cancelBooking(bookingId);
            if (result != null && result.optBoolean("success")) {
                loadBookings();  // เรียก method ของ MyBookings
            }
        }
    }
}
```

**ลักษณะ Composition:**
- ✅ `BookingCard` เป็น inner class ที่ไม่สามารถมีอยู่โดยอิสระ
- ✅ สามารถเข้าถึง members ของ outer class (`MyBookings`) ได้
- ✅ Lifecycle ผูกติดกันอย่างสมบูรณ์

### 4.2 Aggregation (Has-A Relationship แบบหลวม)

#### ตัวอย่างที่ 1: SeatSelection HAS-A Zone
**ตำแหน่ง:** `SeatSelection.java`

```java
public class SeatSelection extends JFrame {
    private Zone currentZone;  // Aggregation
    
    public SeatSelection(int userId, String username, JSONObject concert) {
        // ...
        // รับ Zone ที่มีอยู่แล้วจาก Zone.getAllZones()
        this.currentZone = Zone.getAllZones()[0];
    }
    
    private void switchZone(Zone newZone) {
        this.currentZone = newZone;
        // Zone object มีอยู่อิสระ ไม่ได้ถูกสร้างโดย SeatSelection
    }
}
```

**ลักษณะ Aggregation:**
- ✅ `Zone` ถูกสร้างโดย factory method (`Zone.getAllZones()`)
- ✅ `SeatSelection` เพียงแค่อ้างอิงไปยัง `Zone` ที่มีอยู่แล้ว
- ✅ เมื่อ `SeatSelection` ถูกทำลาย, `Zone` ยังคงมีอยู่

#### ตัวอย่างที่ 2: ConcertList HAS-A JSONObject (Concert Data)
**ตำแหน่ง:** `ConcertList.java`, `ConcertDetail.java`

```java
public class ConcertDetail extends JFrame {
    private JSONObject concert;  // Aggregation
    
    public ConcertDetail(int userId, String username, JSONObject concert) {
        this.concert = concert;  // รับ object ที่มีอยู่แล้ว
        // ไม่ได้สร้าง JSONObject เอง
    }
}
```

**ลักษณะ Aggregation:**
- ✅ `JSONObject` (concert) มาจาก database/API
- ✅ หลายๆ class สามารถอ้างอิงไปยัง JSONObject เดียวกันได้
- ✅ Lifecycle ไม่ผูกติดกัน

#### ตัวอย่างที่ 3: AdminDashboard HAS-A Panels (CardLayout)
**ตำแหน่ง:** `AdminDashboard.java`

```java
public class AdminDashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public AdminDashboard(int userId, String username) {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // แต่ละ panel เป็น object แยก (Aggregation)
        contentPanel.add(new ConcertManagementPanel(), "concerts");
        contentPanel.add(new BookingManagementPanel(), "bookings");
        contentPanel.add(new StatisticsPanel(), "statistics");
        contentPanel.add(new UserManualUploadPanel(), "manuals");
    }
}
```

**ลักษณะ Aggregation:**
- ✅ แต่ละ Panel สามารถมีอยู่อิสระ
- ✅ AdminDashboard เพียงแค่จัดการการแสดงผล
- ✅ Panels สามารถถูก reuse ในที่อื่นได้

### 4.3 สรุปความแตกต่าง

| Aspect | Composition | Aggregation |
|--------|-------------|-------------|
| **Relationship** | Strong "Has-A" | Weak "Has-A" |
| **Lifecycle** | ผูกติดกัน | แยกอิสระ |
| **Ownership** | เจ้าของแต่เพียงผู้เดียว | แชร์ได้ |
| **ตัวอย่าง** | SeatManager → Seats | SeatSelection → Zone |
| **UML Diamond** | Filled ◆ | Hollow ◇ |

---

## 📂 5. การนำเข้าข้อมูลจากไฟล์ (File Input)

### 5.1 การอ่านไฟล์รูปภาพ (Image Files)

**ตำแหน่ง:** `Login.java`, `UIUtils.java`

```java
// Login.java - อ่านไฟล์โลโก้
try {
    ImageIcon logoIcon = new ImageIcon("file/logo.png");
    Image img = logoIcon.getImage();
    int originalWidth = img.getWidth(null);
    int originalHeight = img.getHeight(null);
    int maxSize = 150;
    double scale = Math.min((double)maxSize/originalWidth, 
                            (double)maxSize/originalHeight);
    int scaledWidth = (int)(originalWidth * scale);
    int scaledHeight = (int)(originalHeight * scale);
    Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, 
                                           Image.SCALE_SMOOTH);
    JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
} catch (Exception e) {
    // Fallback to text if image not found
    JLabel logoLabel = new JLabel("ALL CONCERT");
}
```

**ตำแหน่ง:** `UIUtils.java` - Factory Method for Logo

```java
public static JLabel createLogo() {
    try {
        ImageIcon logoIcon = new ImageIcon("file/logo.png");
        Image img = logoIcon.getImage().getScaledInstance(60, 60, 
                                                          Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    } catch (Exception e) {
        JLabel logoText = new JLabel("ALL CONCERT");
        logoText.setFont(LABEL_FONT);
        logoText.setForeground(Color.WHITE);
        return logoText;
    }
}
```

**ประโยชน์:**
- ✅ โหลดไฟล์รูปภาพแบบ dynamic
- ✅ รองรับการ scale/resize
- ✅ มี fallback กรณีไฟล์หาไม่เจอ

### 5.2 การอ่านไฟล์คู่มือผู้ใช้ (Text/PDF/Markdown Files)

**ตำแหน่ง:** `UserManualUploadPanel.java`

```java
import java.nio.file.*;

public class UserManualUploadPanel extends JPanel {
    private static final String MANUAL_DIRECTORY = "manuals/";
    
    /**
     * อ่านไฟล์คู่มือที่มีอยู่
     */
    private void loadCurrentManual() {
        try {
            File manualDir = new File(MANUAL_DIRECTORY);
            if (!manualDir.exists()) {
                logMessage("ℹ️ No manual directory found");
                return;
            }
            
            File[] files = manualDir.listFiles((dir, name) -> 
                name.endsWith(".txt") || 
                name.endsWith(".md") || 
                name.endsWith(".pdf")
            );
            
            if (files != null && files.length > 0) {
                currentManualFile = files[0];
                currentManualLabel.setText("Current: " + 
                                         currentManualFile.getName());
                
                // อ่านเนื้อหาไฟล์ถ้าเป็น text/markdown
                if (currentManualFile.getName().endsWith(".txt") || 
                    currentManualFile.getName().endsWith(".md")) {
                    
                    String content = new String(
                        Files.readAllBytes(currentManualFile.toPath())
                    );
                    previewTextArea.setText(content);
                    logMessage("✅ Loaded: " + currentManualFile.getName());
                }
            }
        } catch (IOException e) {
            logMessage("❌ Error loading manual: " + e.getMessage());
        }
    }
    
    /**
     * อัพโหลด/บันทึกไฟล์คู่มือใหม่
     */
    private void uploadManual(String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        
        // กรองประเภทไฟล์
        FileNameExtensionFilter filter;
        if (fileType.equals("pdf")) {
            filter = new FileNameExtensionFilter("PDF Files", "pdf");
        } else if (fileType.equals("md")) {
            filter = new FileNameExtensionFilter("Markdown Files", "md");
        } else {
            filter = new FileNameExtensionFilter("Text Files", "txt");
        }
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // ตรวจสอบขนาดไฟล์
            long fileSizeInMB = selectedFile.length() / (1024 * 1024);
            if (fileSizeInMB > 10) {
                JOptionPane.showMessageDialog(this,
                    "File too large! Maximum 10 MB",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // คัดลอกไฟล์ไปยังโฟลเดอร์ manuals
                Path targetPath = Paths.get(MANUAL_DIRECTORY + 
                                          selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, 
                          StandardCopyOption.REPLACE_EXISTING);
                
                logMessage("✅ Upload success: " + selectedFile.getName());
                loadCurrentManual();
                
            } catch (IOException e) {
                logMessage("❌ Upload failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * ลบไฟล์คู่มือปัจจุบัน
     */
    private void deleteCurrentManual() {
        if (currentManualFile != null && currentManualFile.exists()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete manual: " + currentManualFile.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Files.delete(currentManualFile.toPath());
                    logMessage("✅ Deleted: " + currentManualFile.getName());
                    currentManualFile = null;
                    loadCurrentManual();
                } catch (IOException e) {
                    logMessage("❌ Delete failed: " + e.getMessage());
                }
            }
        }
    }
}
```

**ฟีเจอร์:**
- ✅ อ่านไฟล์ .txt, .md, .pdf
- ✅ แสดง preview สำหรับไฟล์ text
- ✅ ตรวจสอบขนาดไฟล์ (max 10 MB)
- ✅ รองรับการลบและแทนที่ไฟล์
- ✅ ใช้ `FileNameExtensionFilter` กรองประเภทไฟล์

### 5.3 การอัพโหลดไฟล์รูปภาพ

**ตำแหน่ง:** `ImageUploader.java`

```java
import java.nio.file.Files;

public class ImageUploader {
    /**
     * อัพโหลดรูปภาพไปยัง Catbox.moe
     */
    public static String uploadImage(File imageFile) {
        try {
            // อ่านไฟล์และส่งผ่าน HTTP
            String boundary = "----WebKitFormBoundary" + 
                            System.currentTimeMillis();
            
            HttpURLConnection conn = (HttpURLConnection) 
                new URL(UPLOAD_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", 
                "multipart/form-data; boundary=" + boundary);
            
            try (OutputStream os = conn.getOutputStream()) {
                // ... prepare multipart data
                
                // อ่านและส่งไฟล์
                Files.copy(imageFile.toPath(), os);
                os.flush();
            }
            
            // รับ URL ที่อัพโหลดสำเร็จ
            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                String uploadedUrl = in.readLine();
                return uploadedUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

**ฟีเจอร์:**
- ✅ อ่านไฟล์รูปภาพ (JPEG, PNG, etc.)
- ✅ อัพโหลดผ่าน HTTP multipart/form-data
- ✅ ส่งกลับ URL ของรูปที่อัพโหลด
- ✅ รองรับ progress callback

### 5.4 การสร้างและจัดการโฟลเดอร์

**ตำแหน่ง:** `UserManualUploadPanel.java`

```java
import java.nio.file.*;

private void createManualDirectory() {
    try {
        Files.createDirectories(Paths.get(MANUAL_DIRECTORY));
        logMessage("✅ Manual directory ready");
    } catch (IOException e) {
        logMessage("⚠️ Warning: Could not create manuals directory");
    }
}
```

**ประโยชน์:**
- ✅ สร้างโฟลเดอร์อัตโนมัติถ้ายังไม่มี
- ✅ รองรับการสร้างหลายระดับ (nested directories)

### 5.5 การใช้ JFileChooser

```java
JFileChooser fileChooser = new JFileChooser();
fileChooser.setFileFilter(
    new FileNameExtensionFilter("PDF Files", "pdf")
);

int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
    File selectedFile = fileChooser.getSelectedFile();
    // ประมวลผลไฟล์
}
```

**ประโยชน์:**
- ✅ UI สำหรับเลือกไฟล์
- ✅ กรองประเภทไฟล์ได้
- ✅ รองรับทั้ง open และ save dialog

---

## 🔢 6. การใช้เทคนิคในการเรียงลำดับข้อมูล (Data Sorting)

### 6.1 Sorting ด้วย Comparator และ Lambda

**ตำแหน่ง:** `ConcertSearchSort.java`

```java
public static JSONArray sortConcerts(JSONArray concerts, SortType sortType) {
    // แปลง JSONArray เป็น List เพื่อใช้ sort()
    List<JSONObject> concertList = new ArrayList<>();
    for (int i = 0; i < concerts.length(); i++) {
        concertList.add(concerts.getJSONObject(i));
    }
    
    // เรียงลำดับตาม SortType
    switch (sortType) {
        case DATE_ASC:
            concertList.sort((a, b) -> compareByDate(a, b, true));
            break;
        case DATE_DESC:
            concertList.sort((a, b) -> compareByDate(a, b, false));
            break;
        case PRICE_ASC:
            concertList.sort((a, b) -> compareByPrice(a, b, true));
            break;
        case PRICE_DESC:
            concertList.sort((a, b) -> compareByPrice(a, b, false));
            break;
        case NAME_ASC:
            concertList.sort((a, b) -> compareByName(a, b, true));
            break;
        case NAME_DESC:
            concertList.sort((a, b) -> compareByName(a, b, false));
            break;
    }
    
    // แปลงกลับเป็น JSONArray
    JSONArray sortedArray = new JSONArray();
    for (JSONObject obj : concertList) {
        sortedArray.put(obj);
    }
    
    return sortedArray;
}
```

**เทคนิคที่ใช้:**
- ✅ **Lambda Expression:** `(a, b) -> compareByDate(a, b, true)`
- ✅ **List.sort():** ใช้ Timsort (Stable, O(n log n))
- ✅ **Strategy Pattern:** เลือก comparator ตาม SortType

### 6.2 การเรียงลำดับตามวันที่ (Date Sorting)

```java
private static int compareByDate(JSONObject a, JSONObject b, boolean ascending) {
    String dateA = a.optString("date", "");
    String dateB = b.optString("date", "");
    
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localA = LocalDate.parse(dateA, formatter);
        LocalDate localB = LocalDate.parse(dateB, formatter);
        
        int result = localA.compareTo(localB);
        return ascending ? result : -result;
    } catch (Exception e) {
        // Fallback: string comparison
        return dateA.compareTo(dateB);
    }
}
```

**ฟีเจอร์:**
- ✅ Parse วันที่จาก String format "yyyy-MM-dd"
- ✅ ใช้ `LocalDate.compareTo()` เปรียบเทียบ
- ✅ รองรับทั้ง ascending และ descending
- ✅ มี fallback กรณี parse ไม่สำเร็จ

**ตัวอย่างการใช้งาน:**
```java
// เรียงจากวันที่เร็วสุดไปช้าสุด
JSONArray sorted = ConcertSearchSort.getAllConcertsSorted(SortType.DATE_ASC);

// เรียงจากวันที่ล่าสุดไปเก่าสุด
JSONArray sorted = ConcertSearchSort.getAllConcertsSorted(SortType.DATE_DESC);
```

### 6.3 การเรียงลำดับตามราคา (Price Sorting)

```java
private static int compareByPrice(JSONObject a, JSONObject b, boolean ascending) {
    double priceA = a.optDouble("price", 0);
    double priceB = b.optDouble("price", 0);
    
    int result = Double.compare(priceA, priceB);
    return ascending ? result : -result;
}
```

**ฟีเจอร์:**
- ✅ ใช้ `Double.compare()` เปรียบเทียบตัวเลข
- ✅ รองรับทั้ง ascending (ถูก→แพง) และ descending (แพง→ถูก)
- ✅ Handle missing price (default 0)

**ตัวอย่างการใช้งาน:**
```java
// เรียงจากราคาถูกไปแพง
JSONArray sorted = ConcertSearchSort.sortConcerts(concerts, SortType.PRICE_ASC);

// เรียงจากราคาแพงไปถูก
JSONArray sorted = ConcertSearchSort.sortConcerts(concerts, SortType.PRICE_DESC);
```

### 6.4 การเรียงลำดับตามชื่อ (Alphabetical Sorting)

```java
private static int compareByName(JSONObject a, JSONObject b, boolean ascending) {
    String nameA = a.optString("name", "").toLowerCase();
    String nameB = b.optString("name", "").toLowerCase();
    
    int result = nameA.compareTo(nameB);
    return ascending ? result : -result;
}
```

**ฟีเจอร์:**
- ✅ แปลงเป็น lowercase ก่อนเปรียบเทียบ (case-insensitive)
- ✅ ใช้ `String.compareTo()` เรียงตามพจนานุกรม
- ✅ รองรับทั้ง A-Z และ Z-A

**ตัวอย่างการใช้งาน:**
```java
// เรียงตามชื่อ A-Z
JSONArray sorted = ConcertSearchSort.sortConcerts(concerts, SortType.NAME_ASC);

// เรียงตามชื่อ Z-A
JSONArray sorted = ConcertSearchSort.sortConcerts(concerts, SortType.NAME_DESC);
```

### 6.5 การใช้ Stream API สำหรับ Sorting

**ตำแหน่ง:** `ConcertSearchSort.java` - getAllVenues()

```java
public static List<String> getAllVenues() {
    JSONArray allConcerts = SupabaseConfig.getAllConcerts();
    Set<String> venues = new HashSet<>();
    
    for (int i = 0; i < allConcerts.length(); i++) {
        JSONObject concert = allConcerts.getJSONObject(i);
        String venue = concert.optString("venue", "");
        if (!venue.isEmpty()) {
            venues.add(venue);
        }
    }
    
    // ใช้ Stream API เรียงลำดับและแปลงเป็น List
    return venues.stream()
                 .sorted()
                 .collect(Collectors.toList());
}
```

**เทคนิคที่ใช้:**
- ✅ **Stream.sorted():** เรียงลำดับแบบ natural order
- ✅ **Collectors.toList():** รวบรวมผลลัพธ์เป็น List
- ✅ **Set → Stream → List:** ใช้ Set กำจัดค่าซ้ำก่อนเรียง

### 6.6 Integration กับ UI (ConcertList.java)

```java
// ใน ConcertList.java
private void performSearch() {
    String keyword = searchField.getText().trim();
    
    // ค้นหาและเรียงลำดับพร้อมกัน
    JSONArray results = ConcertSearchSort.searchAndSort(
        keyword, 
        currentSortType
    );
    
    // แสดงผลบน UI
    displayConcerts(results);
}

// ComboBox สำหรับเลือก sort type
sortComboBox.addActionListener(e -> {
    currentSortType = (SortType) sortComboBox.getSelectedItem();
    performSearch();  // Re-sort เมื่อเปลี่ยน sort type
});
```

**ฟีเจอร์:**
- ✅ Real-time sorting เมื่อผู้ใช้เลือก sort option
- ✅ รวม search และ sort เข้าด้วยกัน
- ✅ UI responsive ไม่ค้าง

### 6.7 Performance Considerations

**Algorithm Complexity:**
```
List.sort():     O(n log n)  - Timsort
String compare:  O(m)        - m = string length
Date parse:      O(1)
Double compare:  O(1)

Overall: O(n log n) for n concerts
```

**Optimization:**
- ✅ Parse วันที่เพียงครั้งเดียวต่อการเปรียบเทียบ
- ✅ ใช้ `optString()` แทน `getString()` (ไม่ throw exception)
- ✅ Reuse formatter object

---

## 🔍 7. การใช้เทคนิคในการค้นหาข้อมูล (Data Searching)

### 7.1 Linear Search (Sequential Search)

**ตำแหน่ง:** `ConcertSearchSort.java` - searchConcerts()

```java
public static JSONArray searchConcerts(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
        return SupabaseConfig.getAllConcerts();
    }
    
    JSONArray allConcerts = SupabaseConfig.getAllConcerts();
    JSONArray filteredConcerts = new JSONArray();
    
    String searchTerm = keyword.toLowerCase().trim();
    
    // Linear Search - ตรวจสอบทุก record
    for (int i = 0; i < allConcerts.length(); i++) {
        JSONObject concert = allConcerts.getJSONObject(i);
        
        String name = concert.optString("name", "").toLowerCase();
        String artist = concert.optString("artist", "").toLowerCase();
        String venue = concert.optString("venue", "").toLowerCase();
        
        // ตรวจสอบว่ามี keyword ในฟิลด์ใดฟิลด์หนึ่ง
        if (name.contains(searchTerm) || 
            artist.contains(searchTerm) || 
            venue.contains(searchTerm)) {
            filteredConcerts.put(concert);
        }
    }
    
    return filteredConcerts;
}
```

**เทคนิคที่ใช้:**
- ✅ **Linear Search:** O(n) - ตรวจสอบทุกรายการ
- ✅ **String.contains():** ค้นหา substring
- ✅ **Case-insensitive:** แปลง toLowerCase ก่อนเปรียบเทียบ
- ✅ **Multi-field search:** ค้นหาใน name, artist, venue

**Complexity:**
```
Time:  O(n × m)  - n = จำนวน concerts, m = ความยาว string
Space: O(k)      - k = จำนวนผลลัพธ์ที่เจอ
```

### 7.2 Range Search (ค้นหาตามช่วงราคา)

**ตำแหน่ง:** `ConcertSearchSort.java` - searchConcertsWithPrice()

```java
public static JSONArray searchConcertsWithPrice(String keyword, 
                                                double minPrice, 
                                                double maxPrice) {
    // ค้นหาด้วย keyword ก่อน
    JSONArray filteredConcerts = searchConcerts(keyword);
    JSONArray priceFiltered = new JSONArray();
    
    // กรองด้วยช่วงราคา
    for (int i = 0; i < filteredConcerts.length(); i++) {
        JSONObject concert = filteredConcerts.getJSONObject(i);
        double price = concert.optDouble("price", 0);
        
        boolean passesMinFilter = (minPrice < 0 || price >= minPrice);
        boolean passesMaxFilter = (maxPrice < 0 || price <= maxPrice);
        
        if (passesMinFilter && passesMaxFilter) {
            priceFiltered.put(concert);
        }
    }
    
    return priceFiltered;
}
```

**ฟีเจอร์:**
- ✅ **Range Query:** ค้นหาระหว่าง minPrice ≤ price ≤ maxPrice
- ✅ **Optional filters:** ใช้ -1 แทน "ไม่มีขีดจำกัด"
- ✅ **Chained filtering:** ค้นหา keyword ก่อน แล้วกรองราคา

**ตัวอย่างการใช้งาน:**
```java
// ค้นหาคอนเสิร์ตที่มี "Rock" และราคา 1000-5000 บาท
JSONArray results = ConcertSearchSort.searchConcertsWithPrice(
    "Rock", 
    1000.0, 
    5000.0
);
```

### 7.3 Date Range Search

**ตำแหน่ง:** `ConcertSearchSort.java` - filterByDateRange()

```java
public static JSONArray filterByDateRange(JSONArray concerts, 
                                         String startDate, 
                                         String endDate) {
    JSONArray filtered = new JSONArray();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    try {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        
        for (int i = 0; i < concerts.length(); i++) {
            JSONObject concert = concerts.getJSONObject(i);
            String concertDate = concert.optString("date", "");
            
            try {
                LocalDate date = LocalDate.parse(concertDate, formatter);
                
                // ตรวจสอบว่าอยู่ในช่วงวันที่หรือไม่
                if ((date.isEqual(start) || date.isAfter(start)) && 
                    (date.isEqual(end) || date.isBefore(end))) {
                    filtered.put(concert);
                }
            } catch (Exception e) {
                // Skip invalid dates
            }
        }
    } catch (Exception e) {
        System.err.println("Invalid date format. Use yyyy-MM-dd");
        return concerts;
    }
    
    return filtered;
}
```

**ฟีเจอร์:**
- ✅ Parse วันที่ด้วย `LocalDate`
- ✅ เปรียบเทียบด้วย `isEqual()`, `isAfter()`, `isBefore()`
- ✅ Error handling สำหรับ invalid date format
- ✅ รองรับ inclusive range (start ≤ date ≤ end)

### 7.4 Filter by Category (Venue/Artist)

**ตำแหน่ง:** `ConcertSearchSort.java`

```java
// กรองตามสถานที่
public static JSONArray filterByVenue(JSONArray concerts, String venue) {
    JSONArray filtered = new JSONArray();
    String venueLower = venue.toLowerCase();
    
    for (int i = 0; i < concerts.length(); i++) {
        JSONObject concert = concerts.getJSONObject(i);
        String concertVenue = concert.optString("venue", "").toLowerCase();
        
        if (concertVenue.contains(venueLower)) {
            filtered.put(concert);
        }
    }
    
    return filtered;
}

// กรองตามศิลปิน
public static JSONArray filterByArtist(JSONArray concerts, String artist) {
    JSONArray filtered = new JSONArray();
    String artistLower = artist.toLowerCase();
    
    for (int i = 0; i < concerts.length(); i++) {
        JSONObject concert = concerts.getJSONObject(i);
        String concertArtist = concert.optString("artist", "").toLowerCase();
        
        if (concertArtist.contains(artistLower)) {
            filtered.put(concert);
        }
    }
    
    return filtered;
}
```

**ฟีเจอร์:**
- ✅ Partial match (substring search)
- ✅ Case-insensitive
- ✅ Reusable filter methods

### 7.5 Unique Value Extraction

**ตำแหน่ง:** `ConcertSearchSort.java`

```java
public static List<String> getAllVenues() {
    JSONArray allConcerts = SupabaseConfig.getAllConcerts();
    Set<String> venues = new HashSet<>();  // ใช้ Set กำจัดค่าซ้ำ
    
    for (int i = 0; i < allConcerts.length(); i++) {
        JSONObject concert = allConcerts.getJSONObject(i);
        String venue = concert.optString("venue", "");
        if (!venue.isEmpty()) {
            venues.add(venue);
        }
    }
    
    // เรียงลำดับและส่งกลับเป็น List
    return venues.stream().sorted().collect(Collectors.toList());
}

public static List<String> getAllArtists() {
    JSONArray allConcerts = SupabaseConfig.getAllConcerts();
    Set<String> artists = new HashSet<>();
    
    for (int i = 0; i < allConcerts.length(); i++) {
        JSONObject concert = allConcerts.getJSONObject(i);
        String artist = concert.optString("artist", "");
        if (!artist.isEmpty()) {
            artists.add(artist);
        }
    }
    
    return artists.stream().sorted().collect(Collectors.toList());
}
```

**เทคนิคที่ใช้:**
- ✅ **HashSet:** O(1) insertion, อัตโนมัติกำจัดค่าซ้ำ
- ✅ **Stream API:** เรียงลำดับและแปลง collection
- ✅ **Filter empty:** ตรวจสอบ `!isEmpty()`

**ประโยชน์:**
- ใช้สร้าง dropdown/filter options ใน UI
- แสดงเฉพาะค่าที่ไม่ซ้ำกัน

### 7.6 Combined Search and Sort

**ตำแหน่ง:** `ConcertSearchSort.java` - searchAndSort()

```java
public static JSONArray searchAndSort(String keyword, SortType sortType) {
    // 1. ค้นหาก่อน
    JSONArray searchResults = searchConcerts(keyword);
    
    // 2. เรียงลำดับผลลัพธ์
    return sortConcerts(searchResults, sortType);
}
```

**การใช้งานใน UI:**
```java
// ใน ConcertList.java
private void performSearch() {
    String keyword = searchField.getText().trim();
    SortType sortType = (SortType) sortComboBox.getSelectedItem();
    
    // ค้นหาและเรียงลำดับในครั้งเดียว
    JSONArray results = ConcertSearchSort.searchAndSort(keyword, sortType);
    
    displayConcerts(results);
}
```

### 7.7 Search in Booked Seats

**ตำแหน่ง:** `SeatSelection.java` - loadBookedSeats()

```java
private void loadBookedSeats() {
    JSONArray bookings = SupabaseConfig.getAllBookings();
    bookedSeats = new HashSet<>();
    
    for (int i = 0; i < bookings.length(); i++) {
        JSONObject booking = bookings.getJSONObject(i);
        int bookingConcertId = booking.optInt("concert_id");
        String bookingZone = booking.optString("zone", "");
        
        // ค้นหา booking ที่ตรงกับ concert และ zone
        if (bookingConcertId == concertId && 
            bookingZone.equals(currentZone.getName())) {
            
            String seatsStr = booking.optString("seats", "");
            if (!seatsStr.isEmpty()) {
                String[] seats = seatsStr.split(",");
                for (String seat : seats) {
                    bookedSeats.add(seat.trim());
                }
            }
        }
    }
    
    // Mark seats as booked
    if (seatManager != null) {
        seatManager.markAsBooked(bookedSeats);
    }
}
```

**เทคนิคที่ใช้:**
- ✅ **Exact match:** เปรียบเทียบ concert_id และ zone
- ✅ **String parsing:** แยก seats ด้วย `split(",")`
- ✅ **HashSet lookup:** O(1) สำหรับตรวจสอบว่าที่นั่งถูกจองแล้วหรือไม่

### 7.8 Search Optimization Techniques

#### A. Early Termination
```java
// ถ้าไม่มี keyword ให้ return ทันที
if (keyword == null || keyword.isEmpty()) {
    return SupabaseConfig.getAllConcerts();
}
```

#### B. Pre-processing
```java
// แปลง lowercase ครั้งเดียวก่อน loop
String searchTerm = keyword.toLowerCase().trim();
```

#### C. Index-based Search (สำหรับ HashSet)
```java
// O(1) lookup instead of O(n)
Set<String> bookedSeats = new HashSet<>();
// ...
if (bookedSeats.contains(seatId)) {
    // ที่นั่งถูกจองแล้ว
}
```

### 7.9 Performance Analysis

| Search Type | Algorithm | Time Complexity | Use Case |
|-------------|-----------|-----------------|----------|
| Keyword Search | Linear | O(n × m) | ค้นหาทั่วไป |
| Range Search | Linear | O(n) | กรองราคา/วันที่ |
| Exact Match | Linear | O(n) | ค้นหา ID/zone |
| HashSet Lookup | Hash | O(1) | ตรวจสอบที่นั่งถูกจอง |
| Sorted Search | Binary | O(log n) | ไม่ได้ใช้ในโปรเจ็กต์ |

**Optimization Opportunities:**
- 🔄 ใช้ Binary Search สำหรับข้อมูลที่เรียงแล้ว
- 🔄 Cache ผลลัพธ์การค้นหา
- 🔄 ใช้ Database indexing (Supabase)
- 🔄 Implement pagination สำหรับข้อมูลจำนวนมาก

---

## 📊 สรุป

โปรเจ็กต์ **All Concert Ticket Booking System** เป็นระบบจองตั๋วคอนเสิร์ตที่ครบถ้วน ประกอบด้วย:

### ✅ Checklist ตามภาคผนวก

- ✅ **Inheritance:** 9+ classes สืบทอดจาก JFrame/JPanel
- ✅ **Polymorphism:** Overloading, Override, Interface implementation
- ✅ **Composition/Aggregation:** SeatManager-Seat, SeatSelection-Zone
- ✅ **File Input:** รองรับ image, text, PDF, markdown files
- ✅ **Data Sorting:** 6 รูปแบบ (Date, Price, Name - ASC/DESC)
- ✅ **Data Searching:** Linear search, Range search, Filter, HashSet lookup

### 📈 ขนาดและคุณภาพ

- **Lines of Code:** 4,000+ บรรทัด
- **Classes:** 17 classes
- **Design Patterns:** Factory, Strategy, Composition
- **Error Handling:** Try-catch, fallback mechanisms
- **UI/UX:** Responsive, user-friendly

### 🎯 จุดเด่น

1. **Modular Design:** แยก layer ชัดเจน (UI, Model, Service)
2. **Reusability:** UIUtils, ConcertSearchSort
3. **Scalability:** ใช้ cloud database (Supabase)
4. **User Experience:** Real-time search, dynamic sorting
5. **Admin Features:** จัดการระบบแบบครบวงจร

---

**สร้างเมื่อ:** 5 มกราคม 2026  
**เวอร์ชัน:** 1.0  
**ภาษา:** Java + Swing + Supabase
