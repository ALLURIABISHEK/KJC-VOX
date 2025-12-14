package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


import org.example.dao.AddDepartmentDAO;
import org.example.model.AddDepartment;

import java.io.IOException;

@WebServlet("/api/departments/class")
public class DepartmentByClassServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final AddDepartmentDAO dao = new AddDepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String className = req.getParameter("className");

        resp.setContentType("application/json");

        if (className == null || className.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Missing className parameter\"}");
            return;
        }

        AddDepartment department = dao.getDepartmentByClassName(className);

        if (department != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(department));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\": \"Department not found\"}");
        }
    }
}
