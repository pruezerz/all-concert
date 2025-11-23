import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONArray;

public class SupabaseConfig {
    // TODO: Replace with your actual Supabase credentials
    private static final String SUPABASE_URL = "https://rmzrcpkrjxntgetjwbqr.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJtenJjcGtyanhudGdldGp3YnFyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM4MjU3MzUsImV4cCI6MjA3OTQwMTczNX0.4GrzN8TYRAsAP5y7u4wMRI_wHX_1hXCom_Zv6oS5ol8";
    
    // Register a new user
    public static JSONObject registerUser(String username, String password, String gender, String birthDate) {
        try {
            // Convert dd/mm/yyyy to yyyy-mm-dd for Supabase
            String formattedDate = birthDate;
            if (birthDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] parts = birthDate.split("/");
                formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0]; // yyyy-mm-dd
            }
            
            URL url = new URL(SUPABASE_URL + "/rest/v1/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("gender", gender);
            jsonBody.put("birth_date", formattedDate);
            jsonBody.put("role", "user"); // Default role

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in;
            
            if (responseCode >= 400) {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }
            
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 201) {
                JSONArray arr = new JSONArray(response.toString());
                if (arr.length() > 0) {
                    return arr.getJSONObject(0);
                }
                return new JSONObject(response.toString());
            } else {
                JSONObject error = new JSONObject();
                error.put("error", true);
                error.put("message", response.toString());
                error.put("status", responseCode);
                return error;
            }

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Login user
    public static JSONObject loginUser(String username, String password) {
        try {
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");
            String queryParams = "?username=eq." + encodedUsername + "&password=eq." + encodedPassword + "&select=*";
            
            URL url = new URL(SUPABASE_URL + "/rest/v1/users" + queryParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray results = new JSONArray(response.toString());
            
            if (results.length() > 0) {
                return results.getJSONObject(0);
            } else {
                JSONObject error = new JSONObject();
                error.put("error", true);
                error.put("message", "Invalid username or password");
                return error;
            }

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Check if username exists
    public static boolean usernameExists(String username) {
        try {
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            String queryParams = "?username=eq." + encodedUsername + "&select=username";
            
            URL url = new URL(SUPABASE_URL + "/rest/v1/users" + queryParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray results = new JSONArray(response.toString());
            return results.length() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    // Test connection
    public static boolean testConnection() {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/users?select=count");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== CONCERT METHODS =====
    
    // Get all concerts
    public static JSONArray getAllConcerts() {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?select=*&order=date.asc");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONArray(response.toString());
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    // Get concert by ID
    public static JSONObject getConcertById(int concertId) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?id=eq." + concertId + "&select=*");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray results = new JSONArray(response.toString());
            if (results.length() > 0) {
                return results.getJSONObject(0);
            } else {
                JSONObject error = new JSONObject();
                error.put("error", true);
                error.put("message", "Concert not found");
                return error;
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Search concerts by name or artist
    public static JSONArray searchConcerts(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?or=(name.ilike.*" + encodedKeyword + "*,artist.ilike.*" + encodedKeyword + "*)&select=*");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONArray(response.toString());
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    // ===== BOOKING METHODS =====
    
    // Create a booking
    public static JSONObject createBooking(int userId, int concertId, String seatNumbers, int quantity, double totalPrice) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/bookings");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", userId);
            jsonBody.put("concert_id", concertId);
            jsonBody.put("seat_numbers", seatNumbers);
            jsonBody.put("quantity", quantity);
            jsonBody.put("total_price", totalPrice);
            jsonBody.put("status", "confirmed");

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in;
            
            if (responseCode >= 400) {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }
            
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 201) {
                JSONArray arr = new JSONArray(response.toString());
                if (arr.length() > 0) {
                    return arr.getJSONObject(0);
                }
                return new JSONObject(response.toString());
            } else {
                JSONObject error = new JSONObject();
                error.put("error", true);
                error.put("message", response.toString());
                return error;
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Get user bookings
    public static JSONArray getUserBookings(int userId) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/bookings?user_id=eq." + userId + "&select=*,concerts(*)&order=booking_date.desc");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONArray(response.toString());
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    // Cancel booking
    public static JSONObject cancelBooking(int bookingId) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/bookings?id=eq." + bookingId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("status", "cancelled");

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray arr = new JSONArray(response.toString());
            if (arr.length() > 0) {
                return arr.getJSONObject(0);
            }
            
            JSONObject success = new JSONObject();
            success.put("success", true);
            return success;
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Update concert available seats
    public static boolean updateConcertSeats(int concertId, int newAvailableSeats) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?id=eq." + concertId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("seats_available", newAvailableSeats);

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode == 200 || responseCode == 204;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== ADMIN METHODS =====
    
    // Add new concert
    public static JSONObject addConcert(String name, String artist, String date, String venue, 
                                        double price, int totalSeats, String description, String imageUrl) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("artist", artist);
            jsonBody.put("date", date);
            jsonBody.put("venue", venue);
            jsonBody.put("price", price);
            jsonBody.put("seats_total", totalSeats);
            jsonBody.put("seats_available", totalSeats);
            jsonBody.put("description", description);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                jsonBody.put("image_url", imageUrl);
            }

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in;
            
            if (responseCode >= 400) {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }
            
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 201) {
                JSONArray arr = new JSONArray(response.toString());
                if (arr.length() > 0) {
                    return arr.getJSONObject(0);
                }
                return new JSONObject(response.toString());
            } else {
                JSONObject error = new JSONObject();
                error.put("error", true);
                error.put("message", response.toString());
                return error;
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Update concert
    public static JSONObject updateConcert(int concertId, String name, String artist, String date, 
                                          String venue, double price, int totalSeats, String description, String imageUrl) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?id=eq." + concertId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("artist", artist);
            jsonBody.put("date", date);
            jsonBody.put("venue", venue);
            jsonBody.put("price", price);
            jsonBody.put("seats_total", totalSeats);
            jsonBody.put("description", description);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                jsonBody.put("image_url", imageUrl);
            }

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray arr = new JSONArray(response.toString());
            if (arr.length() > 0) {
                return arr.getJSONObject(0);
            }
            
            JSONObject success = new JSONObject();
            success.put("success", true);
            return success;
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("message", e.getMessage());
            return error;
        }
    }

    // Delete concert
    public static boolean deleteConcert(int concertId) {
        try {
            // First, delete all bookings related to this concert
            URL bookingsUrl = new URL(SUPABASE_URL + "/rest/v1/bookings?concert_id=eq." + concertId);
            HttpURLConnection bookingsConn = (HttpURLConnection) bookingsUrl.openConnection();
            bookingsConn.setRequestMethod("DELETE");
            bookingsConn.setRequestProperty("apikey", SUPABASE_KEY);
            bookingsConn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            bookingsConn.getResponseCode();
            bookingsConn.disconnect();
            
            // Then delete the concert
            URL url = new URL(SUPABASE_URL + "/rest/v1/concerts?id=eq." + concertId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            int responseCode = conn.getResponseCode();
            
            if (responseCode != 200 && responseCode != 204) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Delete failed with code " + responseCode + ": " + response.toString());
            }
            
            conn.disconnect();
            
            return responseCode == 200 || responseCode == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all bookings (for admin)
    public static JSONArray getAllBookings() {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/bookings?select=*,users(username),concerts(name)&order=booking_date.desc");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONArray(response.toString());
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    // Get statistics
    public static JSONObject getStatistics() {
        try {
            JSONObject stats = new JSONObject();
            
            // Count concerts
            JSONArray concerts = getAllConcerts();
            stats.put("totalConcerts", concerts.length());
            
            // Count bookings
            JSONArray bookings = getAllBookings();
            stats.put("totalBookings", bookings.length());
            
            // Calculate revenue
            double totalRevenue = 0;
            for (int i = 0; i < bookings.length(); i++) {
                JSONObject booking = bookings.getJSONObject(i);
                if (booking.getString("status").equals("confirmed")) {
                    totalRevenue += booking.getDouble("total_price");
                }
            }
            stats.put("totalRevenue", totalRevenue);
            
            // Count unique users (approximate)
            stats.put("activeUsers", bookings.length() > 0 ? bookings.length() / 2 : 0);
            
            return stats;
        } catch (Exception e) {
            return new JSONObject();
        }
    }
}
