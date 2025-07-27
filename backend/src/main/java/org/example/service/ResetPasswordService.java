package org.example.service;

import org.bson.Document;
import org.example.repository.ResetPasswordRepository;
import org.example.utils.PasswordUtil;
import org.example.utils.VerificationStore;

public class ResetPasswordService {
    private final ResetPasswordRepository repo = new ResetPasswordRepository();
    private final EmailService emailService = new EmailService();

    public Document verifyRollNumber(String rollNo) {
        return repo.findStudentByRollNo(rollNo);
    }

    public boolean sendVerificationCode(String email) {
        if (!repo.isUserRegistered(email)) {
            return false;
        }

        String code = PasswordUtil.generateVerificationCode();
        VerificationStore.saveResetCode(email, code);

        try {
            emailService.sendResetPasswordCode(email, code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyCodeAndReset(String email, String code, String newPassword) {
        if (!VerificationStore.verifyResetCode(email, code)) {
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        return repo.updatePassword(email, hashedPassword);
    }
}