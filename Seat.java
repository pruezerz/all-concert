/**
 * Represents a seat with its ID and state
 */
public class Seat {
    private final String id;
    private SeatState state;
    
    public enum SeatState {
        AVAILABLE,
        SELECTED,
        BOOKED
    }
    
    public Seat(String id) {
        this(id, SeatState.AVAILABLE);
    }
    
    public Seat(String id, SeatState state) {
        this.id = id;
        this.state = state;
    }
    
    public String getId() {
        return id;
    }
    
    public SeatState getState() {
        return state;
    }
    
    public void setState(SeatState state) {
        this.state = state;
    }
    
    public boolean isAvailable() {
        return state == SeatState.AVAILABLE;
    }
    
    public boolean isSelected() {
        return state == SeatState.SELECTED;
    }
    
    public boolean isBooked() {
        return state == SeatState.BOOKED;
    }
    
    public void select() {
        if (isAvailable()) {
            state = SeatState.SELECTED;
        }
    }
    
    public void deselect() {
        if (isSelected()) {
            state = SeatState.AVAILABLE;
        }
    }
    
    public String getDisplayId() {
        // Extract display ID from full ID (e.g., "BLUEB-A1" -> "A1")
        int dashIndex = id.lastIndexOf("-");
        return dashIndex >= 0 ? id.substring(dashIndex + 1) : id;
    }
    
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
