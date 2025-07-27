package org.example.servlet;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.example.repository.UserRepository;
import org.example.repository.FacultyRepository;
import org.example.model.User;
import org.example.model.Faculty;
import org.example.utils.PasswordUtil;
import com.google.gson.Gson;

import java.io.*;
import java.util.stream.Collectors;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private final UserRepository userRepo = new UserRepository();
    private final FacultyRepository facultyRepo = new FacultyRepository();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String body = req.getReader().lines().collect(Collectors.joining());
            if (body == null || body.trim().isEmpty()) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
                return;
            }

            LoginRequest login = gson.fromJson(body, LoginRequest.class);

            if (login.getEmail() == null || login.getPassword() == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Email and password required");
                return;
            }

            String email = login.getEmail().trim().toLowerCase();
            String password = login.getPassword().trim();

            // ✅ Admin login
            if ("admin@kristujayanti.com".equals(email) && "admin".equals(password)) {
                LoginResponse response = new LoginResponse(
                        true,
                        "Admin login successful",
                        true,
                        email,
                        "Admin",
                        "admin"
                );
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(response));
                return;
            }

            // ✅ Faculty login
            Faculty faculty = facultyRepo.findByEmail(email);
            if (faculty != null) {
                if (!"Faculty@123".equals(password)) {
                    sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid faculty credentials");
                    return;
                }

                LoginResponse response = new LoginResponse(
                        true,
                        "Faculty login successful",
                        true,
                        faculty.getEmail(),
                        faculty.getFullName(),
                        "faculty"
                );
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(response));
                return;
            }

            // ✅ Student login
            User user = userRepo.findByEmail(email);
            if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
                sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid student credentials");
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("isVerified", user.isVerified());

            LoginResponse response = new LoginResponse(
                    true,
                    "Student login successful",
                    user.isVerified(),
                    user.getEmail(),
                    user.getName(),
                    "student"
            );

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        LoginResponse errorResponse = new LoginResponse(false, message, false, null, null, null);
        resp.getWriter().write(gson.toJson(errorResponse));
    }

    // Inner classes
    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public String getPassword() { return password; }

        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }

    static class LoginResponse {
        private final boolean success;
        private final String message;
        private final boolean isVerified;
        private final String email;
        private final String name;
        private final String role;

        public LoginResponse(boolean success, String message, boolean isVerified,
                             String email, String name, String role) {
            this.success = success;
            this.message = message;
            this.isVerified = isVerified;
            this.email = email;
            this.name = name;
            this.role = role;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public boolean isVerified() { return isVerified; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }
}
