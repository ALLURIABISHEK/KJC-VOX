package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.example.model.Feedback;
import org.example.service.FeedbackService;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/submit-feedback")
public class SubmitFeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        Feedback feedback = gson.fromJson(jsonBuffer.toString(), Feedback.class);

        System.out.println("📥 Received feedback from: " + feedback.getStudentEmail());

        feedbackService.submitFeedback(feedback); // ✅ Correct usage

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\": \"Feedback submitted successfully\"}");
    }
}
