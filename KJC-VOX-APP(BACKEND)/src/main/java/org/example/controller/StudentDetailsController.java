package org.example.controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.example.utils.MongoUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "StudentDetailsController", urlPatterns = {"/api/student-details"})
public class StudentDetailsController extends HttpServlet {
    private final MongoDatabase database = MongoUtil.getDatabase();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");

        if (email == null || email.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing or invalid email parameter.");
            return;
        }

        MongoCollection<Document> mcaCollection = database.getCollection("MCA");

        Document query = new Document("email", email);
        Document studentDoc = mcaCollection.find(query).first();

        if (studentDoc != null) {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(studentDoc));
            out.flush();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Student not found.");
        }
    }
}
