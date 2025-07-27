package org.example.servlet;

import jakarta.servlet.http.*;
import org.example.repository.UserRepository;
import org.example.utils.VerificationStore;
import org.example.utils.VerificationStore.RegistrationData;
import com.google.gson.Gson;

import java.io.*;
import java.util.stream.Collectors;

public class RegisterServlet extends HttpServlet {
    private final UserRepository userRepo = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();

        try {
            String body = req.getReader().lines().collect(Collectors.joining());
            RegisterRequest request = gson.fromJson(body, RegisterRequest.class);

            // Log incoming data
            System.out.println("🔔 Registration Attempt:");
            System.out.println("Name: " + request.name);
            System.out.println("Email: " + request.email);
            System.out.println("Code: " + request.code);

            RegistrationData temp = VerificationStore.getRegistrationByEmail(request.email);
            if (temp == null) {
                System.out.println("⚠️ No verification data found for email: " + request.email);
                resp.getWriter().write(gson.toJson(new Response(false, "No verification code found. Please request a new code.")));
                return;
            }

            // Match code
            if (temp.code.equals(request.code)) {
                userRepo.saveUser(temp.name, temp.email, temp.hashedPassword);
                VerificationStore.removeRegistration(request.email);
                System.out.println("✅ Registration successful for: " + request.email);
                resp.getWriter().write(gson.toJson(new Response(true, "Registration successful")));
            } else {
                System.out.println("❌ Verification code mismatch for: " + request.email);
                resp.getWriter().write(gson.toJson(new Response(false, "Invalid or expired verification code.")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write(gson.toJson(new Response(false, "Internal server error: " + e.getMessage())));
        }
    }

    static class RegisterRequest {
        String name;
        String email;
        String code;
    }

    static class Response {
        boolean success;
        String message;
        Response(boolean success, String msg) {
            this.success = success;
            this.message = msg;
        }
    }
}
