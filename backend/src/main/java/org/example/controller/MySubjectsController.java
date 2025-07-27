package org.example.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.AssignSubject;
import org.example.model.SubjectResponse;
import org.example.service.MySubjectsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MySubjectsController", urlPatterns = {"/api/my-subjects"})
public class MySubjectsController extends HttpServlet {
    private final MySubjectsService mySubjectsService = new MySubjectsService();
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

        if (className == null || semesterStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing className or semester");
            return;
        }

        try {
            int semester = Integer.parseInt(semesterStr);
            // Use the new method that returns List<SubjectResponse>
            List<SubjectResponse> subjects = mySubjectsService.getSubjectsForClass(className, semester);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(subjects));
            out.flush();
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid semester format");
        }
    }
}

