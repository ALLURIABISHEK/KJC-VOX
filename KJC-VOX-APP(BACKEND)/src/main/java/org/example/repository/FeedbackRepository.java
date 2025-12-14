package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.Feedback;
import org.example.utils.MongoUtil;

public class FeedbackRepository {
    private final MongoCollection<Document> collection;

    public FeedbackRepository() {
        MongoDatabase database = MongoUtil.getDatabase();
        this.collection = database.getCollection("feedbacks");
    }

    public void saveFeedback(Feedback feedback) {
        Document doc = new Document()
                .append("subject", feedback.getSubject())
                .append("courseCode", feedback.getCourseCode())
                .append("faculty", feedback.getFaculty())
                .append("studentName", feedback.getStudentName())
                .append("studentEmail", feedback.getStudentEmail())
                .append("studentRoll", feedback.getStudentRoll())
                .append("punctual", feedback.getPunctual())
                .append("clarity", feedback.getClarity())
                .append("engaging", feedback.getEngaging())
                .append("pace", feedback.getPace())
                .append("satisfaction", feedback.getSatisfaction())
                .append("comments", feedback.getComments());
        collection.insertOne(doc);
    }
}

