package org.example.service;

import org.example.repository.StudentDetailsRepository;
import org.example.repository.UserRepository;
import org.bson.Document;

public class VerificationService {
    private final StudentDetailsRepository studentRepo = new StudentDetailsRepository();
    private final UserRepository userRepo = new UserRepository();

    public Document getStudentDetails(String email) {
        return studentRepo.findByEmail(email);
    }

    public boolean markAsVerified(String email) {
        return userRepo.markAsVerified(email);
    }


}