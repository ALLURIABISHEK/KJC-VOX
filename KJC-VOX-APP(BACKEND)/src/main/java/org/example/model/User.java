package org.example.model;

import java.util.Date;

public class User {
    private String name;
    private String email;
    private String password;
    private boolean temporaryPassword;
    private Date createdAt;
    private boolean isVerified;

    public User(String name, String email, String password,
                boolean temporaryPassword, Date createdAt, boolean isVerified) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.temporaryPassword = temporaryPassword;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isTemporaryPassword() { return temporaryPassword; }
    public Date getCreatedAt() { return createdAt; }
    public boolean isVerified() { return isVerified; }
}