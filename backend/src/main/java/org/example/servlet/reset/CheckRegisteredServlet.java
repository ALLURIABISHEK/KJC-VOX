package org.example.servlet.reset;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.example.repository.UserRepository;
import java.io.IOException;

@WebServlet("/api/reset/check-registered")

public class CheckRegisteredServlet extends HttpServlet {
    private final UserRepository userRepository;

    public CheckRegisteredServlet() {
        this.userRepository = new UserRepository();
    }



    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {

            String email = req.getParameter("email");

            if (email == null || email.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Email is required\"}");
                return;
            }

            boolean registered = userRepository.findByEmail(email) != null;
            resp.getWriter().write("{\"registered\":" + registered + "}");
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            e.printStackTrace(); // For debugging
        }
    }
}