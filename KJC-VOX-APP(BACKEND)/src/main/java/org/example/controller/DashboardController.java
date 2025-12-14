package org.example.controller;

import org.example.model.*;
import org.example.service.DashboardService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/dashboard/*")
public class DashboardController extends HttpServlet {

    private DashboardService dashboardService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enable CORS
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.getWriter().write(gson.toJson(new ErrorResponse("Invalid endpoint")));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            switch (pathInfo) {
                case "/stats":
                    handleGetStats(response);
                    break;

                case "/student-distribution":
                    handleGetStudentDistribution(response);
                    break;

                case "/faculty-distribution":
                    handleGetFacultyDistribution(response);
                    break;

                case "/semester-distribution":
                    handleGetSemesterDistribution(response);
                    break;

                case "/department-overview":
                    handleGetDepartmentOverview(response);
                    break;

                default:
                    response.getWriter().write(gson.toJson(new ErrorResponse("Endpoint not found")));
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(gson.toJson(new ErrorResponse("Internal server error: " + e.getMessage())));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetStats(HttpServletResponse response) throws IOException {
        DashboardStats stats = dashboardService.getDashboardStats();
        response.getWriter().write(gson.toJson(stats));
    }

    private void handleGetStudentDistribution(HttpServletResponse response) throws IOException {
        List<StudentDistribution> distribution = dashboardService.getStudentDistribution();
        response.getWriter().write(gson.toJson(distribution));
    }

    private void handleGetFacultyDistribution(HttpServletResponse response) throws IOException {
        List<FacultyDistribution> distribution = dashboardService.getFacultyDistribution();
        response.getWriter().write(gson.toJson(distribution));
    }

    private void handleGetSemesterDistribution(HttpServletResponse response) throws IOException {
        List<SemesterDistribution> distribution = dashboardService.getSemesterDistribution();
        response.getWriter().write(gson.toJson(distribution));
    }

    private void handleGetDepartmentOverview(HttpServletResponse response) throws IOException {
        List<DepartmentOverview> overview = dashboardService.getDepartmentOverview();
        response.getWriter().write(gson.toJson(overview));
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}