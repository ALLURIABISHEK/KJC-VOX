package org.example.controller;

import com.google.gson.Gson;
import org.example.service.AuthService;

import static spark.Spark.*;

public class AuthController {
    private static final Gson gson = new Gson();
    private static final AuthService authService = new AuthService();

    public static void routes() {
        post("/api/send-code", (req, res) -> {
            class RequestBody {
                String email;
                String name;
            }

            RequestBody body = gson.fromJson(req.body(), RequestBody.class);
            // Changed from sendVerificationCode to sendCode
            boolean success = authService.sendCode(body.email, body.name);

            res.type("application/json");
            return gson.toJson(new Response(success,
                    success ? "Verification code sent" : "User already exists or error sending email"));
        });

        post("/api/register", (req, res) -> {
            class RequestBody {
                String email;
                String code;
                String name;
            }

            RequestBody body = gson.fromJson(req.body(), RequestBody.class);
            boolean success = authService.verifyAndRegister(body.email, body.code);

            res.type("application/json");
            return gson.toJson(new Response(success,
                    success ? "Registration successful" : "Invalid code or expired"));
        });
    }

    static class Response {
        boolean success;
        String message;

        Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}