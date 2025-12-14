package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        System.out.println("\n🌐🌐🌐 ========== NEW REQUEST TO /api/test-sub-my-subjects ==========");

        // Add CORS headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String faculty = req.getParameter("faculty");
        String className = req.getParameter("className");
        String semesterStr = req.getParameter("semester");

        System.out.println("📥 Received parameters:");
        System.out.println("   faculty: '" + faculty + "'");
        System.out.println("   className: '" + className + "'");
        System.out.println("   semester: '" + semesterStr + "'");

        // Validate required parameters
        if (faculty == null || faculty.trim().isEmpty()) {
            System.out.println("❌ Missing or empty faculty parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty faculty parameter\"}");
            return;
        }

        if (className == null || className.trim().isEmpty()) {
            System.out.println("❌ Missing or empty className parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty className parameter\"}");
            return;
        }

        if (semesterStr == null || semesterStr.trim().isEmpty()) {
            System.out.println("❌ Missing or empty semester parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing or empty semester parameter\"}");
            return;
        }

        try {
            int semester = Integer.parseInt(semesterStr);

            System.out.println("✅ Parameters validated successfully");
            System.out.println("🔄 Calling TestSubService.getSubjectsForFaculty()...");

            // Get subjects assigned to this specific faculty
            List<TestSubResponse> subjects = testSubService.getSubjectsForFaculty(faculty.trim(), className.trim(), semester);

            System.out.println("📊 Service returned " + subjects.size() + " subject(s)");

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();

            if (subjects.isEmpty()) {
                System.out.println("⚠️ No subjects found, returning empty array");
                out.print("{\"message\": \"No subjects assigned to this faculty\", \"subjects\": []}");
            } else {
                String jsonResponse = gson.toJson(subjects);
                System.out.println("✅ Returning JSON response with " + subjects.size() + " subject(s):");
                System.out.println(jsonResponse);
                out.print(jsonResponse);
            }

            out.flush();
            System.out.println("🌐🌐🌐 ========== REQUEST COMPLETED ==========\n");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid semester format: " + semesterStr);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid semester format\"}");
        } catch (Exception e) {
            System.err.println("❌ Internal server error:");
            e.printStackTrace();
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