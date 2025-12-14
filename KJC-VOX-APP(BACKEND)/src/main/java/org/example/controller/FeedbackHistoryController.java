package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.FeedbackHistory;
import org.example.service.FeedbackHistoryService;
import org.example.utils.ObjectIdAdapterHistory;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackHistoryController extends HttpServlet {
    private final FeedbackHistoryService feedbackService;
    private final Gson gson;

    public FeedbackHistoryController() {
        this.feedbackService = new FeedbackHistoryService();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ObjectId.class, new ObjectIdAdapterHistory())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/feedback-history/ - Get all feedback
                List<FeedbackHistory> feedbackList = feedbackService.getAllFeedback();
                out.print(gson.toJson(feedbackList));
            } else {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 3 && pathParts[1].equals("history")) {
                    // GET /api/feedback-history/history/{email}
                    String email = pathParts[2];
                    List<FeedbackHistory> feedbackList = feedbackService.getFeedbackByStudentEmail(email);
                    out.print(gson.toJson(feedbackList));
                } else if (pathParts.length == 2) {
                    // GET /api/feedback-history/{id}
                    String id = pathParts[1];
                    FeedbackHistory feedback = feedbackService.getFeedbackById(id);
                    if (feedback != null) {
                        out.print(gson.toJson(feedback));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print(gson.toJson(new ErrorResponse("Feedback not found")));
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(new ErrorResponse("Invalid request")));
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Failed to retrieve feedback: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo != null && pathInfo.equals("/submit")) {
                // POST /api/feedback-history/submit
                String requestBody = req.getReader().lines().collect(Collectors.joining());
                FeedbackHistory feedback = gson.fromJson(requestBody, FeedbackHistory.class);
                FeedbackHistory savedFeedback = feedbackService.submitFeedback(feedback);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(savedFeedback));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Invalid request")));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Failed to submit feedback: " + e.getMessage())));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");
    }

    private static class ErrorResponse {
        private final String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
