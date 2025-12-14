package org.example.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.example.model.FeedbackHistory;
import org.example.utils.MongoUtil;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class FeedbackHistoryService {
    private final MongoCollection<FeedbackHistory> feedbackCollection;

    public FeedbackHistoryService() {
        MongoDatabase database = MongoUtil.getDatabase();
        this.feedbackCollection = database.getCollection("feedbacks", FeedbackHistory.class);
    }

    // Submit new feedback
    public FeedbackHistory submitFeedback(FeedbackHistory feedback) {
        feedback.setSubmittedAt(java.time.LocalDateTime.now().toString());
        feedbackCollection.insertOne(feedback);
        return feedback;
    }

    // Get feedback history by student email (sorted by newest first)
    public List<FeedbackHistory> getFeedbackByStudentEmail(String studentEmail) {
        List<FeedbackHistory> feedbackList = new ArrayList<>();
        feedbackCollection.find(eq("studentEmail", studentEmail))
                .sort(descending("submittedAt"))
                .into(feedbackList);
        return feedbackList;
    }

    // Get all feedback (for admin)
    public List<FeedbackHistory> getAllFeedback() {
        List<FeedbackHistory> feedbackList = new ArrayList<>();
        feedbackCollection.find()
                .sort(descending("submittedAt"))
                .into(feedbackList);
        return feedbackList;
    }

    // Get feedback by ID
    public FeedbackHistory getFeedbackById(String id) {
        return feedbackCollection.find(eq("_id", new ObjectId(id))).first();
    }
}
