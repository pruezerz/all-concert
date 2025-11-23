import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class ImageUploader {
    // Catbox.moe - Fast, free, no API key needed
    private static final String UPLOAD_URL = "https://catbox.moe/user/api.php";
    
    /**
     * Upload image file to Catbox.moe and return the URL
     * @param imageFile The image file to upload
     * @return The URL of the uploaded image, or null if upload fails
     */
    public static String uploadImage(File imageFile) {
        try {
            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            
            HttpURLConnection conn = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            
            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true)) {
                
                // Add reqtype field
                writer.append("--" + boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"reqtype\"").append("\r\n");
                writer.append("\r\n");
                writer.append("fileupload").append("\r\n");
                
                // Add file field
                writer.append("--" + boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"")
                      .append(imageFile.getName()).append("\"").append("\r\n");
                writer.append("Content-Type: application/octet-stream").append("\r\n");
                writer.append("\r\n");
                writer.flush();
                
                // Write file content
                Files.copy(imageFile.toPath(), os);
                os.flush();
                
                writer.append("\r\n");
                writer.append("--" + boundary + "--").append("\r\n");
                writer.flush();
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String uploadedUrl = in.readLine(); // Catbox returns just the URL
                in.close();
                return uploadedUrl;
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
            
            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            
            progressCallback.onProgress(30, "Preparing upload...");
            
            HttpURLConnection conn = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            
            progressCallback.onProgress(50, "Uploading...");
            
            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true)) {
                
                // Add reqtype field
                writer.append("--" + boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"reqtype\"").append("\r\n");
                writer.append("\r\n");
                writer.append("fileupload").append("\r\n");
                
                // Add file field
                writer.append("--" + boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"")
                      .append(imageFile.getName()).append("\"").append("\r\n");
                writer.append("Content-Type: application/octet-stream").append("\r\n");
                writer.append("\r\n");
                writer.flush();
                
                // Write file content
                Files.copy(imageFile.toPath(), os);
                os.flush();
                
                writer.append("\r\n");
                writer.append("--" + boundary + "--").append("\r\n");
                writer.flush();
            }
            
            progressCallback.onProgress(80, "Processing...");
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String uploadedUrl = in.readLine();
                in.close();
                
                progressCallback.onProgress(100, "Done!");
                return uploadedUrl;
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
