import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * ConcertSearchSort utility class for handling search and sort operations
 * Fetches data from Supabase and performs sorting/filtering in Java
 */
public class ConcertSearchSort {
    
    /**
     * Enum for sort options
     */
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
    
    /**
     * Search concerts by keyword (searches in name, artist, and venue)
     * @param keyword search term
     * @return filtered JSONArray of concerts
     */
    public static JSONArray searchConcerts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return SupabaseConfig.getAllConcerts();
        }
        
        JSONArray allConcerts = SupabaseConfig.getAllConcerts();
        JSONArray filteredConcerts = new JSONArray();
        
        String searchTerm = keyword.toLowerCase().trim();
        
        for (int i = 0; i < allConcerts.length(); i++) {
            JSONObject concert = allConcerts.getJSONObject(i);
            
            String name = concert.optString("name", "").toLowerCase();
            String artist = concert.optString("artist", "").toLowerCase();
            String venue = concert.optString("venue", "").toLowerCase();
            
            // Check if any field contains the search term
            if (name.contains(searchTerm) || artist.contains(searchTerm) || venue.contains(searchTerm)) {
                filteredConcerts.put(concert);
            }
        }
        
        return filteredConcerts;
    }
    
    /**
     * Search concerts with filters
     * @param keyword search term
     * @param minPrice minimum price (use -1 for no limit)
     * @param maxPrice maximum price (use -1 for no limit)
     * @return filtered JSONArray
     */
    public static JSONArray searchConcertsWithPrice(String keyword, double minPrice, double maxPrice) {
        JSONArray filteredConcerts = searchConcerts(keyword);
        JSONArray priceFiltered = new JSONArray();
        
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
    
    /**
     * Sort concerts by specified criteria
     * @param concerts JSONArray of concerts to sort
     * @param sortType sort type (by date, price, name, etc.)
     * @return sorted JSONArray
     */
    public static JSONArray sortConcerts(JSONArray concerts, SortType sortType) {
        // Convert JSONArray to List for easier sorting
        List<JSONObject> concertList = new ArrayList<>();
        for (int i = 0; i < concerts.length(); i++) {
            concertList.add(concerts.getJSONObject(i));
        }
        
        // Perform sorting based on type
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
        
        // Convert back to JSONArray
        JSONArray sortedArray = new JSONArray();
        for (JSONObject obj : concertList) {
            sortedArray.put(obj);
        }
        
        return sortedArray;
    }
    
    /**
     * Combined search and sort
     * @param keyword search term
     * @param sortType sort type
     * @return sorted and filtered JSONArray
     */
    public static JSONArray searchAndSort(String keyword, SortType sortType) {
        JSONArray searchResults = searchConcerts(keyword);
        return sortConcerts(searchResults, sortType);
    }
    
    /**
     * Get all concerts sorted by specified type
     * @param sortType sort type
     * @return sorted JSONArray
     */
    public static JSONArray getAllConcertsSorted(SortType sortType) {
        JSONArray allConcerts = SupabaseConfig.getAllConcerts();
        return sortConcerts(allConcerts, sortType);
    }
    
    /**
     * Filter concerts by date range
     * @param concerts JSONArray of concerts
     * @param startDate start date (yyyy-MM-dd format)
     * @param endDate end date (yyyy-MM-dd format)
     * @return filtered JSONArray
     */
    public static JSONArray filterByDateRange(JSONArray concerts, String startDate, String endDate) {
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
                    if ((date.isEqual(start) || date.isAfter(start)) && 
                        (date.isEqual(end) || date.isBefore(end))) {
                        filtered.put(concert);
                    }
                } catch (Exception e) {
                    // Skip concerts with invalid date format
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid date format. Use yyyy-MM-dd");
            return concerts;
        }
        
        return filtered;
    }
    
    /**
     * Filter concerts by venue
     * @param concerts JSONArray of concerts
     * @param venue venue name to filter by
     * @return filtered JSONArray
     */
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
    
    /**
     * Filter concerts by artist
     * @param concerts JSONArray of concerts
     * @param artist artist name to filter by
     * @return filtered JSONArray
     */
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
    
    /**
     * Get all unique venues from concerts
     * @return List of venue names
     */
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
        
        return venues.stream().sorted().collect(Collectors.toList());
    }
    
    /**
     * Get all unique artists from concerts
     * @return List of artist names
     */
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
    
    /**
     * Compare two concerts by date
     * @param a first concert
     * @param b second concert
     * @param ascending true for ascending order
     * @return comparison result
     */
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
            return dateA.compareTo(dateB);
        }
    }
    
    /**
     * Compare two concerts by price
     * @param a first concert
     * @param b second concert
     * @param ascending true for ascending order
     * @return comparison result
     */
    private static int compareByPrice(JSONObject a, JSONObject b, boolean ascending) {
        double priceA = a.optDouble("price", 0);
        double priceB = b.optDouble("price", 0);
        
        int result = Double.compare(priceA, priceB);
        return ascending ? result : -result;
    }
    
    /**
     * Compare two concerts by name
     * @param a first concert
     * @param b second concert
     * @param ascending true for ascending order
     * @return comparison result
     */
    private static int compareByName(JSONObject a, JSONObject b, boolean ascending) {
        String nameA = a.optString("name", "").toLowerCase();
        String nameB = b.optString("name", "").toLowerCase();
        
        int result = nameA.compareTo(nameB);
        return ascending ? result : -result;
    }
}
