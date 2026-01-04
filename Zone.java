import java.awt.Color;

/**
 * Represents a concert zone with its properties
 */
public class Zone {
    private final String name;
    private final Color color;
    private final double price;
    private final ZoneType type;
    
    public enum ZoneType {
        SEATING,    // PURPLE, GREEN, BLUE - have individual seats
        STANDING    // YELLOW, PINK - standing areas
    }
    
    public Zone(String name, Color color, double price, ZoneType type) {
        this.name = name;
        this.color = color;
        this.price = price;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public Color getColor() {
        return color;
    }
    
    public double getPrice() {
        return price;
    }
    
    public ZoneType getType() {
        return type;
    }
    
    public boolean hasSeats() {
        return type == ZoneType.SEATING;
    }
    
    public String getPrefix() {
        return name.replace(" ", "");
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    // Factory method for creating zones
    public static Zone[] getAllZones() {
        return new Zone[] {
            new Zone("PURPLE A", new Color(150, 100, 200), 6000.0, ZoneType.SEATING),
            new Zone("GREEN A", new Color(100, 200, 100), 4000.0, ZoneType.SEATING),
            new Zone("BLUE A", new Color(100, 150, 220), 3000.0, ZoneType.SEATING),
            new Zone("BLUE B", new Color(100, 150, 220), 3000.0, ZoneType.SEATING),
            new Zone("GREEN B", new Color(100, 200, 100), 4000.0, ZoneType.SEATING),
            new Zone("PURPLE B", new Color(150, 100, 200), 6000.0, ZoneType.SEATING),
            new Zone("Yellow", new Color(230, 230, 100), 2000.0, ZoneType.STANDING),
            new Zone("PINK", new Color(230, 150, 180), 1000.0, ZoneType.STANDING)
        };
    }
    
    public static Zone getZoneByName(String name) {
        for (Zone zone : getAllZones()) {
            if (zone.getName().equals(name) || zone.getName().startsWith(name)) {
                return zone;
            }
        }
        return null;
    }
}
