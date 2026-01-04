```mermaid
classDiagram
    %% ============ UI Frame Classes ============
    class Login {
        -username: String
        -password: String
        +Login()
        +performLogin() void
    }

    class Register {
        -username: String
        -password: String
        -gender: String
        -birthDate: String
        +Register()
        +performRegister() void
        +validateInput() boolean
    }

    class ConcertList {
        -userId: int
        -username: String
        -concertGridPanel: JPanel
        -searchField: JTextField
        -sortComboBox: JComboBox
        -currentSortType: SortType
        +ConcertList(int, String)
        +createTopBar() JPanel
        +createContentArea() JScrollPane
        +loadConcerts() void
        +performSearch() void
    }

    class ConcertDetail {
        -userId: int
        -username: String
        -concert: JSONObject
        +ConcertDetail(int, String, JSONObject)
        +createContentPanel() JPanel
        +showBookingInfo() void
    }

    class SeatSelection {
        -userId: int
        -username: String
        -concert: JSONObject
        -concertId: int
        -currentZone: Zone
        -seatManager: SeatManager
        -bookedSeats: Set<String>
        +SeatSelection(int, String, JSONObject)
        +createZoneMap() JPanel
        +createSeatPanel() JPanel
        +updateSeatGrid() void
        +loadBookedSeats() void
    }

    class PaymentPage {
        -userId: int
        -username: String
        -concert: JSONObject
        -selectedZone: String
        -selectedSeats: Set<String>
        -totalPrice: double
        -bookingReference: String
        +PaymentPage(int, String, JSONObject, String, Set, double)
        +createPaymentDetails() JPanel
        +createQRCodePanel() JPanel
        +generateBookingReference() String
    }

    class MyBookings {
        -userId: int
        -username: String
        -bookingsPanel: JPanel
        +MyBookings(int, String)
        +createBookingsPanel() JScrollPane
        +loadBookings() void
        +BookingCard: InnerClass
    }

    class MyBookings.BookingCard {
        -booking: JSONObject
        +BookingCard(JSONObject)
        +createInfoLabel(String, String) JPanel
        +cancelBooking(int) void
    }

    class AdminDashboard {
        -userId: int
        +AdminDashboard(int)
        +createTopBar() JPanel
        +createStatisticsPanel() JPanel
        +createManagementPanel() JPanel
    }

    %% ============ Utility Classes ============
    class UIUtils {
        {static} DARK_RED: Color
        {static} HEADER_RED: Color
        {static} DARK_INPUT: Color
        {static} LIGHT_GRAY: Color
        {static} TITLE_FONT: Font
        {static} BUTTON_FONT: Font
        {static} createLogo() JLabel
        {static} createIconButton(String, String, String) JButton
        {static} createBackButton(JFrame, int, String) JButton
        {static} createLogoutButton(JFrame) JButton
        {static} createBookingButton(JFrame, int, String) JButton
        {static} createBasicTopBar(JFrame, int, String) JPanel
        {static} createTopBarWithTitle(JFrame, int, String, String) JPanel
        {static} createTopBarWithBooking(JFrame, int, String) JPanel
    }

    class SupabaseConfig {
        {static} SUPABASE_URL: String
        {static} SUPABASE_KEY: String
        {static} registerUser(String, String, String, String) JSONObject
        {static} loginUser(String, String) JSONObject
        {static} usernameExists(String) boolean
        {static} getAllConcerts() JSONArray
        {static} getConcertById(int) JSONObject
        {static} createBooking(...) JSONObject
        {static} getUserBookings(int) JSONArray
        {static} getAllBookings() JSONArray
        {static} cancelBooking(int) JSONObject
        {static} getUserProfile(int) JSONObject
        {static} updateUserProfile(...) JSONObject
    }

    class ConcertSearchSort {
        {static} searchConcerts(String) JSONArray
        {static} searchConcertsWithPrice(String, double, double) JSONArray
        {static} sortConcerts(JSONArray, SortType) JSONArray
        {static} searchAndSort(String, SortType) JSONArray
        {static} getAllConcertsSorted(SortType) JSONArray
        {static} filterByDateRange(JSONArray, String, String) JSONArray
        {static} filterByVenue(JSONArray, String) JSONArray
        {static} filterByArtist(JSONArray, String) JSONArray
        {static} getAllVenues() List~String~
        {static} getAllArtists() List~String~
    }

    class ConcertSearchSort.SortType {
        DATE_ASC
        DATE_DESC
        PRICE_ASC
        PRICE_DESC
        NAME_ASC
        NAME_DESC
    }

    %% ============ Model Classes ============
    class Seat {
        -id: String
        -state: SeatState
        +Seat(String)
        +Seat(String, SeatState)
        +getId() String
        +getState() SeatState
        +setState(SeatState) void
        +isAvailable() boolean
        +isSelected() boolean
        +isBooked() boolean
        +select() void
        +deselect() void
        +getDisplayId() String
    }

    class Seat.SeatState {
        AVAILABLE
        SELECTED
        BOOKED
    }

    class SeatManager {
        -seats: Map~String, Seat~
        -currentZone: Zone
        -totalPrice: double
        +SeatManager(Zone)
        +initializeSeats() void
        +markAsBooked(Set~String~) void
        +toggleSeat(String) boolean
        +clearSelection() void
        +getSelectedSeatIds() List~String~
        +getSelectedCount() int
        +getTotalPrice() double
        +getZone() Zone
        +getAllSeats() Collection~Seat~
        +getSeat(String) Seat
    }

    class Zone {
        -name: String
        -color: Color
        -price: double
        -type: ZoneType
        -prefix: String
        +Zone(String, Color, double, ZoneType)
        +getName() String
        +getColor() Color
        +getPrice() double
        +getType() ZoneType
        +getPrefix() String
        +hasSeats() boolean
        {static} getZoneByName(String) Zone
    }

    class Zone.ZoneType {
        SEATING
        STANDING
        VIP
    }

    %% ============ Other Classes ============
    class ImageUploader {
        -selectedFile: File
        +ImageUploader()
        +selectImage() File
        +uploadImage(File) boolean
        +saveImageLocally(File) void
    }

    class UserManualUploadPanel {
        -manualTextArea: JTextArea
        +UserManualUploadPanel()
        +createPanel() JPanel
        +saveManual() void
        +loadManual() void
    }

    %% ============ Relationships ============
    
    %% Login/Register to ConcertList
    Login --> ConcertList: opens
    Register --> ConcertList: opens after registration
    
    %% ConcertList navigation
    ConcertList --> ConcertDetail: navigates to
    ConcertList --> MyBookings: navigates to
    
    %% ConcertDetail navigation
    ConcertDetail --> SeatSelection: navigates to
    ConcertDetail --> MyBookings: navigates to
    ConcertDetail --> ConcertList: back button
    
    %% SeatSelection navigation
    SeatSelection --> PaymentPage: navigates to
    SeatSelection --> ConcertDetail: back button
    SeatSelection --> SeatManager: uses
    
    %% PaymentPage navigation
    PaymentPage --> ConcertList: after success
    
    %% MyBookings navigation
    MyBookings --> ConcertList: back button
    MyBookings --> MyBookings.BookingCard: contains
    
    %% Model relationships
    SeatSelection --> Seat: displays
    SeatSelection --> Zone: manages zones
    SeatManager --> Seat: manages
    SeatManager --> Zone: operates on
    Seat --> Seat.SeatState: has state
    Zone --> Zone.ZoneType: has type
    
    %% Utility relationships
    Login --> UIUtils: uses
    Register --> UIUtils: uses
    ConcertList --> UIUtils: uses
    ConcertDetail --> UIUtils: uses
    SeatSelection --> UIUtils: uses
    PaymentPage --> UIUtils: uses
    MyBookings --> UIUtils: uses
    AdminDashboard --> UIUtils: uses
    
    %% API relationships
    Login --> SupabaseConfig: calls loginUser()
    Register --> SupabaseConfig: calls registerUser()
    ConcertList --> SupabaseConfig: calls getAllConcerts()
    ConcertList --> ConcertSearchSort: uses for search/sort
    ConcertDetail --> SupabaseConfig: calls getConcertById()
    SeatSelection --> SupabaseConfig: calls getAllBookings(), createBooking()
    PaymentPage --> SupabaseConfig: calls createBooking()
    MyBookings --> SupabaseConfig: calls getUserBookings(), cancelBooking()
    AdminDashboard --> SupabaseConfig: calls various admin methods
    
    %% ConcertSearchSort relationships
    ConcertSearchSort --> ConcertSearchSort.SortType: uses
    ConcertSearchSort --> SupabaseConfig: fetches data from
    
    %% Other relationships
    AdminDashboard --> ImageUploader: uses
    AdminDashboard --> UserManualUploadPanel: uses
```

## 📊 UML Class Diagram - All Concert System (Updated)

### 🎯 Overview
- **Total Classes**: 17 (including inner classes)
- **Main Packages**: UI Frames, Model Classes, Utilities, API Config
- **Relationships**: Navigation flows, Composition, Dependency

---

## 🏗️ Architecture Layers

### 📱 **UI Layer** (Presentation)
```
Login → Register → ConcertList → ConcertDetail → SeatSelection → PaymentPage → MyBookings
```
- Swing-based JFrame components
- Direct user interaction
- Uses UIUtils for consistent styling

### 🔧 **Model/Domain Layer**
```
Seat → SeatManager → Zone
```
- Business logic for seat management
- Data structures

### 🌐 **Service/API Layer**
```
SupabaseConfig (Static methods for database access)
ConcertSearchSort (Search and sorting operations)
```
- Database operations
- Data filtering and sorting

### 🛠️ **Utility Layer**
```
UIUtils (UI component creation)
ImageUploader (File handling)
UserManualUploadPanel (Content management)
```
- Reusable components
- Helper functions

---

## 📌 Key Relationships

### Navigation Flow (User Journey)
```
Login/Register 
    ↓
ConcertList (search, sort, view concerts)
    ├─→ ConcertDetail (view concert info)
    │       └─→ SeatSelection (choose seats)
    │               └─→ PaymentPage (payment)
    └─→ MyBookings (view booking history)
```

### Data Flow
```
UI Frames ← ConcertSearchSort ← SupabaseConfig (Supabase DB)
UI Frames ← SeatManager ← Seat/Zone
```

### Composition
```
SeatSelection "has a" SeatManager
SeatManager "has many" Seat
MyBookings "contains" BookingCard (inner class)
```

---

## 🔐 Security & Access Control
- SupabaseConfig uses static methods (centralized API access)
- userId and username passed through UI frames for session management
- Admin features in AdminDashboard (separate from user flow)

---

## 📈 Scalability Notes
- **UIUtils**: Centralized component creation (DRY principle)
- **ConcertSearchSort**: Separates search logic from UI
- **SupabaseConfig**: All DB operations in one place
- **Model classes** (Seat, Zone): Independent of UI

---
