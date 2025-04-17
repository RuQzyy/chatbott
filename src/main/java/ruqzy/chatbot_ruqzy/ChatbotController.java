package ruqzy.chatbot_ruqzy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ChatbotController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message").toLowerCase();
        Map<String, String> response = new HashMap<>();

        if (message.contains("siapa pencipta chatbot") || message.contains("siapa yang buat chat bot")) {
            response.put("reply", "Project RuQzyy Chatbot ini dibuat oleh Muhammad Al-Faruq 🚀");
        } else {
            String aiReply = geminiService.generateResponse(message);
            response.put("reply", aiReply);
        }

        return ResponseEntity.ok(response);
    }
}
