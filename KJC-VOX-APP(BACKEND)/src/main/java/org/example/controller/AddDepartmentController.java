package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


import org.example.dao.AddDepartmentDAO;
import org.example.model.AddDepartment;

import java.io.*;
@WebServlet("/api/departments")
public class AddDepartmentController extends HttpServlet {
    private final Gson gson = new Gson();
    private final AddDepartmentDAO dao = new AddDepartmentDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        AddDepartment department = gson.fromJson(reader, AddDepartment.class);

        boolean success = dao.insertOrUpdateDepartment(department);

        resp.setContentType("application/json");
        if (success) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Department added/updated successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Failed to add/update department\"}");
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String className = req.getParameter("className");

        resp.setContentType("application/json");

        if (className == null || className.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Missing className parameter\"}");
            return;
        }

        boolean deleted = dao.deleteDepartmentByClassName(className);

        if (deleted) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Department deleted successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\": \"Department not found or failed to delete\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(dao.getAllDepartments()));
    }
}
