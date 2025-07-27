package org.example.controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.example.utils.MongoUtil;

import java.io.IOException;
import java.util.*;

@WebServlet("/api/feedback-summary")
public class FeedbackSummaryController extends HttpServlet {

    private final MongoCollection<Document> collection =
            MongoUtil.getDatabase().getCollection("feedbacks");
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String subject = req.getParameter("subject");
        String faculty = req.getParameter("faculty");

        if (subject == null || faculty == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing parameters\"}");
            return;
        }

        List<Document> feedbacks = collection.find(
                Filters.and(
                        Filters.eq("subject", subject),
                        Filters.eq("faculty", faculty)
                )
        ).into(new ArrayList<>());

        int total = feedbacks.size();
        int punctualYes = 0;
        int engagingYes = 0;
        double claritySum = 0;
        int normal = 0, fast = 0, slow = 0;
        List<String> comments = new ArrayList<>();

        for (Document f : feedbacks) {
            if ("yes".equalsIgnoreCase(f.getString("punctual"))) punctualYes++;
            if ("yes".equalsIgnoreCase(f.getString("engaging"))) engagingYes++;

            Object clarityObj = f.get("clarity");
            if (clarityObj instanceof Number) {
                claritySum += ((Number) clarityObj).doubleValue();
            }

            String pace = f.getString("pace");
            if ("normal".equalsIgnoreCase(pace)) normal++;
            else if ("fast".equalsIgnoreCase(pace)) fast++;
            else if ("slow".equalsIgnoreCase(pace)) slow++;

            String comment = f.getString("comments");
            if (comment != null && !comment.trim().isEmpty()) {
                comments.add(comment);
            }
        }

        Map<String, Object> paceMap = new HashMap<>();
        paceMap.put("normal", normal);
        paceMap.put("fast", fast);
        paceMap.put("slow", slow);

        Map<String, Object> result = new HashMap<>();
        result.put("punctuality", total > 0 ? (punctualYes * 100 / total) + "%" : "0%");
        result.put("clarity", total > 0 ? String.format("%.1f", claritySum / total) + "/5" : "0/5");
        result.put("engagement", total > 0 ? (engagingYes * 100 / total) + "%" : "0%");
        result.put("pace", paceMap);
        result.put("comments", comments);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(result));
    }
}
