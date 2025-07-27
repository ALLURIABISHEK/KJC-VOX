package org.example.servlet.reset;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.example.service.ResetPasswordService;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/api/reset/password")
public class ResetPasswordServlet extends HttpServlet {
    private final ResetPasswordService service = new ResetPasswordService();
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String email = req.getParameter("email");
            String code = req.getParameter("code");
            String newPassword = req.getParameter("newPassword");

            if (email == null || code == null || newPassword == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\":false,\"message\":\"All fields are required\"}");
                return;
            }

            boolean success = service.verifyCodeAndReset(email, code, newPassword);
            if (!success) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"success\":false,\"message\":\"Invalid code or email\"}");
                return;
            }

            resp.getWriter().write("{\"success\":true,\"message\":\"Password reset successfully\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"message\":\"Password reset failed\"}");
            resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
                e.printStackTrace(); // For debugging
        }
    }
}

