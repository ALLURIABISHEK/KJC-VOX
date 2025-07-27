package org.example.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.TestSubResponse;
import org.example.service.TestSubService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "TestSubController", urlPatterns = {"/api/test-sub-my-subjects"})
public class TestSubController extends HttpServlet {
    private final TestSubService testSubService = new TestSubService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Add CORS headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String faculty = req.getParameter("faculty");
        String className = req.getParameter("className");
        String semesterStr = req.getParameter("semester");

        // Validate required parameters
        if (faculty == null || faculty.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty faculty parameter\"}");
            return;
        }

        if (className == null || className.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty className parameter\"}");
            return;
        }

        if (semesterStr == null || semesterStr.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty semester parameter\"}");
            return;
        }

        try {
            int semester = Integer.parseInt(semesterStr);

            // Get subjects assigned to this specific faculty
            List<TestSubResponse> subjects = testSubService.getSubjectsForFaculty(faculty.trim(), className.trim(), semester);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();

            if (subjects.isEmpty()) {
                out.print("{\"message\": \"No subjects assigned to this faculty\", \"subjects\": []}");
            } else {
                out.print(gson.toJson(subjects));
            }

            out.flush();
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid semester format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle preflight CORS requests
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}