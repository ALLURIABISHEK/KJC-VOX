package org.example.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationStore {
    // For registration flow
    public static class RegistrationData {
        public String name;
        public String email;
        public String code;
        public String hashedPassword;
        public LocalDateTime createdAt;

        public RegistrationData(String name, String email, String code, String hashedPassword) {
            this.name = name;
            this.email = email;
            this.code = code;
            this.hashedPassword = hashedPassword;
            this.createdAt = LocalDateTime.now();
        }
    }

    // For password reset flow
    public static class ResetData {
        public String email;
        public String code;
        public LocalDateTime createdAt;

        public ResetData(String email, String code) {
            this.email = email;
            this.code = code;
            this.createdAt = LocalDateTime.now();
        }
    }

    // Stores for different flows
    private static final Map<String, RegistrationData> registrationStore = new HashMap<>();
    private static final Map<String, ResetData> resetStore = new HashMap<>();

    // Registration flow methods
    public static void saveRegistration(String name, String email, String code, String hashedPassword) {
        registrationStore.put(email, new RegistrationData(name, email, code, hashedPassword));
    }

    public static RegistrationData getRegistrationByEmail(String email) {
        return registrationStore.get(email);
    }

    public static void removeRegistration(String email) {
        registrationStore.remove(email);
    }

    // Password reset flow methods
    public static void saveResetCode(String email, String code) {
        resetStore.put(email, new ResetData(email, code));
    }

    public static boolean verifyResetCode(String email, String code) {
        ResetData data = resetStore.get(email);
        if (data == null) return false;

        // Check if code matches and is not expired (5 minutes expiry)
        boolean isValid = data.code.equals(code) &&
                LocalDateTime.now().isBefore(data.createdAt.plusMinutes(5));

        if (isValid) {
            resetStore.remove(email); // One-time use code
        }
        return isValid;
    }

    // Cleanup expired entries (can be called periodically)
    public static void cleanupExpiredEntries() {
        LocalDateTime now = LocalDateTime.now();

        // Clean registration store
        registrationStore.entrySet().removeIf(entry ->
                now.isAfter(entry.getValue().createdAt.plusHours(24))
        );

        // Clean reset store
        resetStore.entrySet().removeIf(entry ->
                now.isAfter(entry.getValue().createdAt.plusMinutes(30))
        );
    }
}