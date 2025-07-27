package org.example.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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
