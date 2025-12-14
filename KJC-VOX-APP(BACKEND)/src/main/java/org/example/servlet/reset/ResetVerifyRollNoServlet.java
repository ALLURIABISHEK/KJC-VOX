package org.example.servlet.reset;

import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import org.bson.Document;
import org.example.service.ResetPasswordService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/reset/verify-rollno")
public class ResetVerifyRollNoServlet extends HttpServlet {
    private final ResetPasswordService service = new ResetPasswordService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String rollNo = req.getParameter("rollno");
            if (rollNo == null || rollNo.isEmpty()) {
                resp.setStatus(400);
                out.write("{\"error\":\"Roll number is required\"}");
                return;
            }

            Document student = service.verifyRollNumber(rollNo);
            if (student == null) {
                resp.setStatus(404);
                out.write("{\"error\":\"Student not found\"}");
                return;
            }

            out.write(student.toJson());
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            e.printStackTrace();
        }
    }
}