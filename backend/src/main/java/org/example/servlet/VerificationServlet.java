package org.example.servlet;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.example.repository.UserRepository;
import com.google.gson.Gson;

import java.io.*;
import java.util.stream.Collectors;

@WebServlet("/api/verify-user")
public class VerificationServlet extends HttpServlet {
    private final UserRepository userRepo = new UserRepository();
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Get email from session or request body
        String email = req.getParameter("email");
        if (email == null) {
            // Alternative: Read from request body
            BufferedReader reader = req.getReader();
            String body = reader.lines().collect(Collectors.joining());
            email = gson.fromJson(body, EmailRequest.class).email;
        }

        if (email == null || email.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"Email required\"}");
            return;
        }

        boolean success = userRepo.markAsVerified(email);
        if (success) {
            resp.getWriter().write("{\"success\":true,\"message\":\"User verified\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"success\":false,\"message\":\"User not found\"}");
        }
    }

    static class EmailRequest {
        String email;
    }
}