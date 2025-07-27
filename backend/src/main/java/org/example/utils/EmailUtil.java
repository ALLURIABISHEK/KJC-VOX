package org.example.utils;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private static final String from = "abiprojectsworking@gmail.com";
    private static final String password = "dscnsvpboxcerdif";

    public static void sendCodeAndPassword(String to, String name, String code, String passwordPlain) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("KJC VOX | Registration Verification");
        message.setText("Hi " + name + ",\n\n" +
                "Your temporary credentials:\n" +
                "Verification Code: " + code + "\n" +
                "Temporary Password: " + passwordPlain + "\n\n" +
                "Use these to complete your registration.\n\n-KJC VOX Team");

        Transport.send(message);
    }
}