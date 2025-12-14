package org.example.controller;

import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.example.model.Notice;
import org.example.repository.NoticeRepository;
import org.example.service.NoticeService;
import org.example.utils.MongoUtil;

import java.io.*;
import java.util.List;

@WebServlet("/api/notices")
public class NoticeController extends HttpServlet {
    private NoticeService noticeService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        try {
            noticeService = new NoticeService(new NoticeRepository(MongoUtil.getDatabase()));
        } catch (Exception e) {
            System.err.println("Failed to initialize NoticeService: " + e.getMessage());
            throw new ServletException("Could not initialize NoticeService", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set CORS headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            // Read JSON from request body
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse JSON to Notice object
            Notice notice = gson.fromJson(sb.toString(), Notice.class);

            // Validate
            if (notice.getTitle() == null || notice.getDate() == null ||
                    notice.getNoticeFor() == null || notice.getNoticeType() == null ||
                    notice.getDescription() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing required fields\"}");
                return;
            }

            // Save notice
            noticeService.createNotice(notice);

            // Success response
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Notice created successfully\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Failed to create notice: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String noticeFor = req.getParameter("for");  // e.g., student/faculty/both
            if (noticeFor == null || noticeFor.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing 'for' query parameter\"}");
                return;
            }

            // Fetch notices for given type (student/faculty/both)
            List<Notice> notices = noticeService.getNoticesFor(noticeFor);

            String json = new Gson().toJson(notices);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Failed to fetch notices\"}");
            e.printStackTrace();
        }
    }

}

