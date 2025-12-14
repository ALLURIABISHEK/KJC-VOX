package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String from = "abiprojectsworking@gmail.com";
    private static final String password = "dscnsvpboxcerdif";

    public void sendVerificationEmail(String toEmail, String name, String verificationCode, String tempPassword)
            throws MessagingException {
        // SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // Compose email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("KJC VOX - Account Verification");

        String emailBody = "Dear " + name + ",\n\n" +
                "Your verification code: " + verificationCode + "\n" +
                "Temporary password: " + tempPassword + "\n\n" +
                "Use these credentials to login and complete your registration.\n\n" +
                "Regards,\nKJC VOX Team";

        message.setText(emailBody);
        Transport.send(message);
    }

    public void sendResetPasswordCode(String toEmail, String verificationCode) throws MessagingException {
        // SMTP configuration (same as above)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // Compose email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("KJC VOX - Password Reset Verification");

        String emailBody = "Dear User,\n\n" +
                "Your password reset verification code is: " + verificationCode + "\n\n" +
                "This code will expire in 30 minutes.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Regards,\nKJC VOX Team";

        message.setText(emailBody);
        Transport.send(message);
    }
}