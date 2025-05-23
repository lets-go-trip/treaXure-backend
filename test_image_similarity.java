import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.ai.openai.client.OpenAiClient;
import org.springframework.ai.openai.api.OpenAiApi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test_image_similarity {
    public static void main(String[] args) throws Exception {
        // URLs of images to compare
        String referenceImageUrl = "https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg";
        String targetImageUrl = "https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp";

        System.out.println("Loading reference image from: " + referenceImageUrl);
        byte[] referenceImageBytes = loadImageFromUrl(referenceImageUrl);
        System.out.println("Reference image loaded, size: " + referenceImageBytes.length + " bytes");

        System.out.println("Loading target image from: " + targetImageUrl);
        byte[] targetImageBytes = loadImageFromUrl(targetImageUrl);
        System.out.println("Target image loaded, size: " + targetImageBytes.length + " bytes");

        System.out.println("Converting images to base64...");
        String referenceImageBase64 = Base64.getEncoder().encodeToString(referenceImageBytes);
        String targetImageBase64 = Base64.getEncoder().encodeToString(targetImageBytes);

        System.out.println("Reference image base64 (first 100 chars): " + referenceImageBase64.substring(0, Math.min(100, referenceImageBase64.length())));
        System.out.println("Target image base64 (first 100 chars): " + targetImageBase64.substring(0, Math.min(100, targetImageBase64.length())));

        System.out.println("\nImages loaded successfully. To use the actual OpenAI API for comparison:");
        System.out.println("1. Set the OPENAI_API_KEY environment variable");
        System.out.println("2. Use the following curl command to test the embeddings API:");
        
        System.out.println("\nFor reference image:");
        System.out.println("curl https://api.openai.com/v1/embeddings \\");
        System.out.println("  -H \"Content-Type: application/json\" \\");
        System.out.println("  -H \"Authorization: Bearer $OPENAI_API_KEY\" \\");
        System.out.println("  -d '{");
        System.out.println("    \"model\": \"clip\",");
        System.out.println("    \"input\": [{\"type\": \"image_base64\", \"image_base64\": {\"data\": \"" + referenceImageBase64.substring(0, 20) + "...\"}}]");
        System.out.println("  }'");

        System.out.println("\nFor target image:");
        System.out.println("curl https://api.openai.com/v1/embeddings \\");
        System.out.println("  -H \"Content-Type: application/json\" \\");
        System.out.println("  -H \"Authorization: Bearer $OPENAI_API_KEY\" \\");
        System.out.println("  -d '{");
        System.out.println("    \"model\": \"clip\",");
        System.out.println("    \"input\": [{\"type\": \"image_base64\", \"image_base64\": {\"data\": \"" + targetImageBase64.substring(0, 20) + "...\"}}]");
        System.out.println("  }'");
    }

    private static byte[] loadImageFromUrl(String imageUrl) throws IOException {
        System.out.println("Fetching image from URL: " + imageUrl);
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        // Read the image
        BufferedImage image = ImageIO.read(connection.getInputStream());
        if (image == null) {
            throw new IOException("Could not read image from URL: " + imageUrl);
        }
        
        // Convert to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, getImageFormatFromUrl(imageUrl), baos);
        return baos.toByteArray();
    }
    
    private static String getImageFormatFromUrl(String url) {
        String lowercaseUrl = url.toLowerCase();
        if (lowercaseUrl.endsWith(".jpg") || lowercaseUrl.endsWith(".jpeg")) {
            return "jpg";
        } else if (lowercaseUrl.endsWith(".png")) {
            return "png";
        } else if (lowercaseUrl.endsWith(".gif")) {
            return "gif";
        } else if (lowercaseUrl.endsWith(".bmp")) {
            return "bmp";
        } else if (lowercaseUrl.endsWith(".webp")) {
            return "webp";
        } else {
            // Default to PNG
            return "png";
        }
    }
} 