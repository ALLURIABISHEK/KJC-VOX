package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Faculty;
import org.example.service.FacultyService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/api/faculty/*")
public class FacultyController extends HttpServlet {
    private final FacultyService facultyService = new FacultyService();
    private final Gson gson = new Gson();

    // Helper method to validate ObjectId format
    private boolean isValidObjectId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String trimmedId = id.trim();
        return trimmedId.length() == 24 && trimmedId.matches("[0-9a-fA-F]{24}");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try (PrintWriter out = resp.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAllFaculty(req, resp, out);
            } else if (pathInfo.equals("/search")) {
                handleSearchFaculty(req, resp, out);
            } else if (pathInfo.equals("/stats")) {
                handleGetStats(req, resp, out);
            } else if (pathInfo.equals("/removed/count")) {
                handleGetRemovedFacultyCount(resp, out); // ✅ Added method
            } else {
                String id = pathInfo.substring(1);
                handleGetFacultyById(id, resp, out);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    private void handleGetRemovedFacultyCount(HttpServletResponse resp, PrintWriter out) {
        long count = facultyService.getRemovedFacultyCount();
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print("{\"removedCount\": " + count + "}");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            Faculty faculty = gson.fromJson(req.getReader(), Faculty.class);

            if (!isValidFaculty(faculty)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Missing required fields\"}");
                return;
            }

            // ✅ Check for duplicate facultyId or email
            Faculty savedFaculty = facultyService.addFaculty(faculty);

            if (savedFaculty != null) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(savedFaculty));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Failed to save faculty to database\"}");
            }

        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Invalid JSON format\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Email is required in the URL\"}");
            return;
        }

        // ✅ Extract email from the URL (e.g., /api/faculty/nani@gmail.com)
        String email = pathInfo.substring(1); // removes leading "/"

        try (PrintWriter out = resp.getWriter()) {
            Faculty updatedData = gson.fromJson(req.getReader(), Faculty.class);

            if (!isValidFaculty(updatedData)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Missing required fields\"}");
                return;
            }

            System.out.println("PUT request - update by email: " + email);

            Faculty updated = facultyService.updateFacultyByEmail(email, updatedData);

            if (updated != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(updated));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Faculty not found with email: " + email + "\"}");
            }

        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Identifier is required in URL\"}");
            return;
        }

        String identifier = pathInfo.substring(1); // remove leading slash

        try (PrintWriter out = resp.getWriter()) {
            if (identifier.startsWith("by-email/")) {
                // DELETE by email
                String email = identifier.substring("by-email/".length());
                System.out.println("DELETE request - by email: " + email);

                boolean deleted = facultyService.deleteFacultyByEmail(email);
                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"message\": \"Faculty deleted by email: " + email + "\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Faculty not found with email: " + email + "\"}");
                }
            } else {
                // DELETE by ObjectId
                System.out.println("DELETE request - Mongo ID: " + identifier);
                if (!isValidObjectId(identifier)) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String errorMsg = String.format("Invalid MongoDB ObjectId format: '%s' (length: %d). ObjectId must be exactly 24 hexadecimal characters.", identifier, identifier.length());
                    out.print("{\"error\": \"" + errorMsg + "\"}");
                    return;
                }

                boolean deleted = facultyService.deleteFaculty(identifier);
                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"message\": \"Faculty deleted successfully: " + identifier + "\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Faculty not found with ID: " + identifier + "\"}");
                }
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    private void handleGetAllFaculty(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        String sortBy = req.getParameter("sortBy");
        String sortOrder = req.getParameter("sortOrder");
        String pageStr = req.getParameter("page");
        String limitStr = req.getParameter("limit");

        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;

        List<Faculty> facultyList = facultyService.getAllFaculty(sortBy, sortOrder, page, limit);
        System.out.println("Faculty list before JSON conversion: " + facultyList.toString());

        resp.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(facultyList));
    }

    private void handleSearchFaculty(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String departmentType = req.getParameter("departmentType");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        List<Faculty> facultyList = facultyService.searchFaculty(fullName, email, departmentType, fromDate, toDate);
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(facultyList));
    }

    private void handleGetStats(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        Map<String, Object> stats = facultyService.getDepartmentStats();
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(stats));
    }

    private void handleGetFacultyById(String id, HttpServletResponse resp, PrintWriter out) {
        // Add debugging for GET by ID
        System.out.println("GET by ID - received id: '" + id + "'");

        // Validate ObjectId format for GET requests too
        if (!isValidObjectId(id)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String errorMsg = String.format("Invalid MongoDB ObjectId format: '%s' (length: %d). " +
                    "ObjectId must be exactly 24 hexadecimal characters.", id, id.length());
            out.print("{\"error\": \"" + errorMsg + "\"}");
            return;
        }

        Faculty faculty = facultyService.getFacultyById(id);

        if (faculty != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(faculty));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Faculty not found with ID: " + id + "\"}");
        }
    }

    private boolean isValidFaculty(Faculty faculty) {
        return faculty != null &&
                faculty.getFacultyId() != null && !faculty.getFacultyId().trim().isEmpty() &&
                faculty.getFullName() != null && !faculty.getFullName().trim().isEmpty() &&
                faculty.getEmail() != null && !faculty.getEmail().trim().isEmpty() &&
                faculty.getDepartment() != null && !faculty.getDepartment().trim().isEmpty() &&
                faculty.getJoiningDate() != null && !faculty.getJoiningDate().trim().isEmpty() &&
                faculty.getDepartmentType() != null && !faculty.getDepartmentType().trim().isEmpty();
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
    private void handleGetRemovalCount(HttpServletResponse resp, PrintWriter out) {
        long count = facultyService.getRemovedFacultyCount();
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print("{\"count\": " + count + "}");
    }

}