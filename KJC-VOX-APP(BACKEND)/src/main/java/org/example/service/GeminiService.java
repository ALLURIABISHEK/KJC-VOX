package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiService {

    private static final String GEMINI_API_KEY = "your code here";

    // Updated to use a current model from your list
    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // =======================================================================

    public ValidationResult validateFeedback(String comments) {
        try {
            String prompt = buildPrompt(comments);
            String response = callGeminiAPI(prompt);
            return parseGeminiResponse(response, comments);

        } catch (Exception e) {
            System.err.println("⚠ GEMINI FAILURE — Auto-approved\n" + e.getMessage());
            e.printStackTrace();
            return new ValidationResult(true, null, "AI unavailable — feedback accepted");
        }
    }

    // =======================================================================

    private String buildPrompt(String comments) {
        return "You are a feedback validator. Analyze the following student feedback and respond with ONLY valid JSON.\n\n" +
                "Feedback: \"" + comments + "\"\n\n" +
                "Rules:\n" +
                "1. Reject if rude, abusive, disrespectful, insulting, or contains profanity\n" +
                "2. Accept if positive, neutral, or constructively critical\n" +
                "3. If rejected, provide 2 polite alternative phrasings\n\n" +
                "Respond with ONLY this JSON structure (no markdown, no extra text):\n" +
                "{\n" +
                "  \"approved\": true,\n" +
                "  \"reason\": \"Feedback is constructive\",\n" +
                "  \"suggestions\": []\n" +
                "}\n\n" +
                "OR if rejected:\n" +
                "{\n" +
                "  \"approved\": false,\n" +
                "  \"reason\": \"Contains inappropriate language\",\n" +
                "  \"suggestions\": [\"suggestion 1\", \"suggestion 2\"]\n" +
                "}";
    }

    // =======================================================================

    private String callGeminiAPI(String prompt) throws IOException {
        JsonObject body = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();

        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        body.add("contents", contents);

        // Add generation config for more reliable JSON output
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.2);
        generationConfig.addProperty("topP", 0.8);
        generationConfig.addProperty("topK", 40);
        body.add("generationConfig", generationConfig);

        RequestBody requestBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            String errorBody = response.body() != null ? response.body().string() : "No error body";
            throw new IOException("Gemini Error " + response.code() + " → " + errorBody);
        }

        return response.body().string();
    }

    // =======================================================================

    private ValidationResult parseGeminiResponse(String json, String originalComments) {
        try {
            System.out.println("📥 Raw Gemini Response: " + json);

            JsonObject root = gson.fromJson(json, JsonObject.class);
            JsonArray candidates = safeArray(root, "candidates");

            if (candidates == null || candidates.size() == 0) {
                return autoApprove("No candidates returned");
            }

            JsonObject candidate = candidates.get(0).getAsJsonObject();
            JsonObject content = safeObject(candidate, "content");
            JsonArray parts = safeArray(content, "parts");

            if (parts == null || parts.size() == 0) {
                return autoApprove("No response parts");
            }

            String text = parts.get(0).getAsJsonObject().get("text").getAsString();
            System.out.println("📄 Extracted Text: " + text);

            // Clean up the response
            text = text.trim();
            text = text.replaceAll("```json\\s*", "");
            text = text.replaceAll("```\\s*", "");
            text = text.trim();

            JsonObject data = gson.fromJson(text, JsonObject.class);
            System.out.println("🔍 Parsed JSON: " + data);

            // Safe extraction with null checks
            boolean approved = true; // Default to approved
            if (data.has("approved")) {
                JsonElement approvedElement = data.get("approved");
                if (approvedElement != null && !approvedElement.isJsonNull()) {
                    approved = approvedElement.getAsBoolean();
                }
            }

            String reason = "No reason provided";
            if (data.has("reason")) {
                JsonElement reasonElement = data.get("reason");
                if (reasonElement != null && !reasonElement.isJsonNull()) {
                    reason = reasonElement.getAsString();
                }
            }

            List<String> suggestions = new ArrayList<>();
            if (data.has("suggestions")) {
                JsonElement suggestionsElement = data.get("suggestions");
                if (suggestionsElement != null && suggestionsElement.isJsonArray()) {
                    suggestionsElement.getAsJsonArray().forEach(e -> {
                        if (e != null && !e.isJsonNull()) {
                            suggestions.add(e.getAsString());
                        }
                    });
                }
            }

            System.out.println("✅ Validation Result: approved=" + approved + ", reason=" + reason);

            return new ValidationResult(
                    approved,
                    suggestions.isEmpty() ? null : suggestions,
                    reason
            );

        } catch (Exception e) {
            System.err.println("❌ Parse Error: " + e.getMessage());
            e.printStackTrace();
            return autoApprove("Parse failed → " + e.getMessage());
        }
    }

    // =======================================================================

    private JsonArray safeArray(JsonObject o, String key) {
        if (o == null || !o.has(key)) return null;
        JsonElement element = o.get(key);
        return (element != null && element.isJsonArray()) ? element.getAsJsonArray() : null;
    }

    private JsonObject safeObject(JsonObject o, String key) {
        if (o == null || !o.has(key)) return null;
        JsonElement element = o.get(key);
        return (element != null && element.isJsonObject()) ? element.getAsJsonObject() : null;
    }

    private ValidationResult autoApprove(String message) {
        System.out.println("⚠ Auto-approving: " + message);
        return new ValidationResult(true, null, message + " | Auto-approved");
    }

    // =======================================================================

    public static class ValidationResult {
        private boolean approved;
        private List<String> suggestions;
        private String message;

        public ValidationResult(boolean approved, List<String> suggestions, String message) {
            this.approved = approved;
            this.suggestions = suggestions;
            this.message = message;
        }

        public boolean isApproved() { return approved; }
        public List<String> getSuggestions() { return suggestions; }
        public String getMessage() { return message; }
    }
}
