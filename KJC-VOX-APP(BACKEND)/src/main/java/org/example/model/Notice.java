package org.example.model;

import org.bson.types.ObjectId;

public class Notice {
    private ObjectId id;
    private String title;
    private String date;
    private String noticeFor;
    private String noticeType;
    private String description;

    // Getters
    public ObjectId getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getNoticeFor() { return noticeFor; }
    public String getNoticeType() { return noticeType; }
    public String getDescription() { return description; }

    // Setters
    public void setId(ObjectId id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setNoticeFor(String noticeFor) { this.noticeFor = noticeFor; }
    public void setNoticeType(String noticeType) { this.noticeType = noticeType; }
    public void setDescription(String description) { this.description = description; }
}