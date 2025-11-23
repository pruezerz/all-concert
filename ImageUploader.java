import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Base64;
import org.json.JSONObject;

public class ImageUploader {
    // ImgBB API key (free tier - replace with your own from https://api.imgbb.com/)
    private static final String API_KEY = "e89e5cf373575cf53b63cdc65435fc89";
    private static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";
    
    /**
     * Upload image file to ImgBB and return the URL
     * @param imageFile The image file to upload
     * @return The URL of the uploaded image, or null if upload fails
     */
    public static String uploadImage(File imageFile) {
        try {
            // Read file and convert to base64
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            String encodedImage = Base64.getEncoder().encodeToString(fileContent);
            
            // Create connection
            URL url = new URL(UPLOAD_URL + "?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            // Prepare form data
            String formData = "image=" + URLEncoder.encode(encodedImage, "UTF-8");
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = formData.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.getBoolean("success")) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                    return data.getString("url"); // Return the image URL
                }
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Upload image with progress callback
     * @param imageFile The image file to upload
     * @param progressCallback Callback for upload progress (0-100)
     * @return The URL of the uploaded image, or null if upload fails
     */
    public static String uploadImageWithProgress(File imageFile, ProgressCallback progressCallback) {
        try {
            progressCallback.onProgress(10, "Reading file...");
            
            // Read file and convert to base64
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            progressCallback.onProgress(30, "Encoding image...");
            
            String encodedImage = Base64.getEncoder().encodeToString(fileContent);
            progressCallback.onProgress(50, "Uploading...");
            
            // Create connection
            URL url = new URL(UPLOAD_URL + "?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            // Prepare form data
            String formData = "image=" + URLEncoder.encode(encodedImage, "UTF-8");
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = formData.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
            
            progressCallback.onProgress(80, "Processing...");
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                progressCallback.onProgress(90, "Completing...");
                
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.getBoolean("success")) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                    String imageUrl = data.getString("url");
                    progressCallback.onProgress(100, "Done!");
                    return imageUrl;
                }
            }
            
            progressCallback.onProgress(0, "Upload failed");
            return null;
        } catch (Exception e) {
            progressCallback.onProgress(0, "Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Interface for upload progress callback
     */
    public interface ProgressCallback {
        void onProgress(int percentage, String message);
    }
}
