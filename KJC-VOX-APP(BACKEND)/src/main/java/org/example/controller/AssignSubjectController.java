package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.example.model.AssignSubject;
import org.example.repository.AssignSubjectRepository;
import org.example.service.AssignSubjectService;
import org.example.utils.MongoUtil;

import java.io.*;

@WebServlet("/api/assign-subjects") // optional if you're using ServletHolder
public class AssignSubjectController extends HttpServlet {
    private AssignSubjectService service;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        service = new AssignSubjectService(
                new AssignSubjectRepository(MongoUtil.getDatabase())
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        BufferedReader reader = req.getReader();
        AssignSubject assignSubject = gson.fromJson(reader, AssignSubject.class);

        if (service.assignSubjects(assignSubject)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Subjects assigned successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\": \"Duplicate assignment detected. Assignment failed.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String className = req.getParameter("className");
        String semesterStr = req.getParameter("semester");
        String facultyName = req.getParameter("faculty");

        if (className == null || semesterStr == null || facultyName == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing parameters: className, semester, or faculty\"}");
            return;
        }

        int semester;
        try {
            semester = Integer.parseInt(semesterStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid semester format\"}");
            return;
        }

        AssignSubject result = service.getAssignedSubjects(className, semester, facultyName);
        if (result != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(result));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\": \"No subjects assigned for this faculty\"}");
        }
    }

}
