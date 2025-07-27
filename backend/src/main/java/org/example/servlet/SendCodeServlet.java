package org.example.servlet;

import jakarta.servlet.http.*;
import org.example.utils.EmailUtil;
import org.example.utils.PasswordUtil;
import org.example.utils.VerificationStore;
import com.google.gson.Gson;

import java.io.*;
import java.util.stream.Collectors;

public class SendCodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        BufferedReader reader = req.getReader();
        String body = reader.lines().collect(Collectors.joining());
        SendCodeRequest json = new Gson().fromJson(body, SendCodeRequest.class);

        String code = PasswordUtil.generateVerificationCode();  // e.g., 6-digit
        String plainPassword = PasswordUtil.generateRandomPassword();  // Random password
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);  // Hash it

        // ✅ Save into VerificationStore (for later registration confirmation)
        VerificationStore.saveRegistration(json.name, json.email, code, hashedPassword);

        try {
            // ✅ Send Email (code + password to user email)
            EmailUtil.sendCodeAndPassword(json.email, json.name, code, plainPassword);
            resp.getWriter().write("{\"success\": true, \"message\": \"Verification code and password sent to email.\"}");
        } catch (Exception e) {
            // ✅ Email failed
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"success\": false, \"message\": \"Failed to send email. Please try again later.\"}");
        }
    }

    static class SendCodeRequest {
        String name;
        String email;
    }
}
