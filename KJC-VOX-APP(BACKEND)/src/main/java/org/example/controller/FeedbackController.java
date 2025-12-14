package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Feedback;
import org.example.service.FeedbackService;
import org.example.service.GeminiService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();
    private final GeminiService geminiService = new GeminiService();
    private final Gson gson = new Gson();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Basic CORS headers (adjust origin in production)
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // Handle preflight
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = req.getRequestURI();

        try {
            if ("POST".equalsIgnoreCase(req.getMethod())) {
                if (path.endsWith("/submit-feedback")) {
                    handleSubmitFeedback(req, res);
                    return;
                } else if (path.endsWith("/validate-feedback")) {
                    handleValidateFeedback(req, res);
                    return;
                }
            }

            // default: not found
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("error", "Invalid API endpoint");
            res.getWriter().write(gson.toJson(notFound));

        } catch (Exception ex) {
            ex.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Internal server error");
            res.getWriter().write(gson.toJson(error));
        }
    }

    private void handleSubmitFeedback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String body = readBody(req);
        Feedback feedback = gson.fromJson(body, Feedback.class);

        if (feedback == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid request body");
            res.getWriter().write(gson.toJson(error));
            return;
        }

        if (feedback.getStudentName() == null || feedback.getStudentEmail() == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Missing required student information");
            res.getWriter().write(gson.toJson(error));
            return;
        }

        // Persist feedback (service handles storage)
        feedbackService.submitFeedback(feedback);

        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        success.put("message", "Feedback submitted successfully");
        res.getWriter().write(gson.toJson(success));
    }

    private void handleValidateFeedback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String body = readBody(req);
        Map<String, String> map = gson.fromJson(body, Map.class);
        String comments = map != null ? map.get("comments") : null;

        if (comments == null || comments.trim().isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Comments cannot be empty");
            res.getWriter().write(gson.toJson(error));
            return;
        }

        GeminiService.ValidationResult result = geminiService.validateFeedback(comments);

        Map<String, Object> response = new HashMap<>();
        response.put("approved", result.isApproved());
        response.put("suggestions", result.getSuggestions() != null ? result.getSuggestions() : new String[] {});
        response.put("message", result.getMessage() != null ? result.getMessage() : "");
        res.getWriter().write(gson.toJson(response));
    }

    private String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
