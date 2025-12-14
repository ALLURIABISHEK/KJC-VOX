package org.example.service;

import org.example.model.Feedback;
import org.example.repository.FeedbackRepository;

public class FeedbackService {
    private final FeedbackRepository repository = new FeedbackRepository();

    public void submitFeedback(Feedback feedback) {
        repository.saveFeedback(feedback);
    }
}