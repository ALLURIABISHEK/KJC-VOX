package org.example.service;

import org.example.repository.UserRepository;
import org.example.utils.PasswordUtil;
import org.example.utils.VerificationStore;
import org.example.utils.VerificationStore.RegistrationData;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();
    private final EmailService emailService = new EmailService();

    // Renamed from sendVerificationCode to sendCode to match controller
    public boolean sendCode(String email, String name) {
        if (userRepository.findByEmail(email) != null) {
            return false; // already registered
        }

        String plainPassword = PasswordUtil.generateRandomPassword();
        String code = PasswordUtil.generateVerificationCode();
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        // Cache it
        VerificationStore.saveRegistration(name, email, code, hashedPassword);

        // Send via email
        try {
            emailService.sendVerificationEmail(email, name, code, plainPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyAndRegister(String email, String code) {
        RegistrationData temp = VerificationStore.getRegistrationByEmail(email);
        if (temp == null || !temp.code.equals(code)) {
            return false;
        }

        // Save to DB
        userRepository.saveUser(temp.name, temp.email, temp.hashedPassword);
        VerificationStore.removeRegistration(email);
        return true;
    }
}