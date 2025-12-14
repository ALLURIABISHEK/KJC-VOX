package org.example.servlet;

import com.google.gson.JsonObject;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import org.example.repository.UserRepository;
import org.example.service.VerificationService;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/finalize-verification")
public class FinalizeVerificationServlet extends HttpServlet {
    private final UserRepository userRepo = new UserRepository();
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            // Read JSON body
            BufferedReader reader = req.getReader();
            String body = reader.lines().collect(Collectors.joining());
            JsonObject json = gson.fromJson(body, JsonObject.class);
            String email = json.get("email").getAsString();

            if (email == null || email.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\":false,\"message\":\"Email is required\"}");
                return;
            }

            // Update verification status in database
            boolean success = userRepo.markAsVerified(email);

            if (success) {
                resp.getWriter().write("{\"success\":true,\"message\":\"Verification completed\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\":false,\"message\":\"Failed to verify user\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}