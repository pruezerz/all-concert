import java.util.*;

/**
 * Manages seat selection logic and state
 */
public class SeatManager {
    private final Map<String, Seat> seats;
    private final Zone currentZone;
    private double totalPrice;
    
    public SeatManager(Zone zone) {
        this.currentZone = zone;
        this.seats = new HashMap<>();
        this.totalPrice = 0.0;
        initializeSeats();
    }
    
    private void initializeSeats() {
        if (currentZone.hasSeats()) {
            char[] rows = {'A', 'B', 'C', 'D'};
            for (char row : rows) {
                for (int num = 1; num <= 7; num++) {
                    String seatId = currentZone.getPrefix() + "-" + row + num;
                    seats.put(seatId, new Seat(seatId));
                }
            }
        }
    }
    
    public void markAsBooked(Set<String> bookedSeatIds) {
        for (String seatId : bookedSeatIds) {
            Seat seat = seats.get(seatId);
            if (seat != null) {
                seat.setState(Seat.SeatState.BOOKED);
            }
        }
    }
    
    public boolean toggleSeat(String seatId) {
        Seat seat = seats.get(seatId);
        if (seat == null || seat.isBooked()) {
            return false;
        }
        
        if (seat.isSelected()) {
            seat.deselect();
            totalPrice -= currentZone.getPrice();
        } else {
            seat.select();
            totalPrice += currentZone.getPrice();
        }
        return true;
    }
    
    public void clearSelection() {
        for (Seat seat : seats.values()) {
            if (seat.isSelected()) {
                seat.deselect();
            }
        }
        totalPrice = 0.0;
    }
    
    public List<String> getSelectedSeatIds() {
        List<String> selected = new ArrayList<>();
        for (Seat seat : seats.values()) {
            if (seat.isSelected()) {
                selected.add(seat.getId());
            }
        }
        return selected;
    }
    
    public int getSelectedCount() {
        int count = 0;
        for (Seat seat : seats.values()) {
            if (seat.isSelected()) {
                count++;
            }
        }
        return count;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public Zone getZone() {
        return currentZone;
    }
    
    public Collection<Seat> getAllSeats() {
        return seats.values();
    }
    
    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }
}
