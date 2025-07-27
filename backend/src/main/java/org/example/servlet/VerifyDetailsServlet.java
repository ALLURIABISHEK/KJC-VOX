package org.example.servlet;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.bson.Document;
import org.example.service.VerificationService;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/api/verify-details")
public class VerifyDetailsServlet extends HttpServlet {
    private final VerificationService verificationService = new VerificationService();
    private final Gson gson = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String email = req.getParameter("email");

        if (email == null || email.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Email parameter is required\"}");
            return;
        }

        Document studentDetails = verificationService.getStudentDetails(email);
        if (studentDetails == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Student details not found\"}");
            return;
        }

        // Convert to JSON response
        resp.getWriter().write(studentDetails.toJson());
    }
}