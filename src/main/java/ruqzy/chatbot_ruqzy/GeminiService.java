package ruqzy.chatbot_ruqzy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateResponse(String message) {
        try {
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;


            String safeMessage = message.replace("\"", "\\\"");
            String jsonInput = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + safeMessage + "\" } ] } ] }";
    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();
    
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Raw Response: " + response.body());
    
            if (response.statusCode() != 200) {
                return "Gagal: " + response.statusCode() + " - " + response.body();
            }
    
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                return parts.get(0).getAsJsonObject().get("text").getAsString();
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return "Terjadi kesalahan saat menghubungi Gemini API.";
    }
    
    
}
