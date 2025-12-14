package org.example.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateVerificationCode() {
        return String.format("%06d", random.nextInt(999999));
    }

    public static String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
