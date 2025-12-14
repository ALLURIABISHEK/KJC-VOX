package org.example.servlet.reset;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import org.example.service.ResetPasswordService;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/api/reset/send-code")
public class ResetSendCodeServlet extends HttpServlet {
    private final ResetPasswordService service = new ResetPasswordService();
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String email = req.getParameter("email");
            if (email == null || email.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\":false,\"message\":\"Email is required\"}");
                return;
            }

            boolean success = service.sendVerificationCode(email);
            if (!success) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"success\":false,\"message\":\"Account not registered\"}");
                return;
            }

            resp.getWriter().write("{\"success\":true,\"message\":\"Verification code sent\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"message\":\"Failed to send code\"}");
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            e.printStackTrace();
        }
    }
}