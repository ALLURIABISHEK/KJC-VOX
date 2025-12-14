package org.example.model;

import org.bson.types.ObjectId;

public class FeedbackHistory {
    private ObjectId _id;
    private String subject;
    private String courseCode;
    private String faculty;
    private String studentName;
    private String studentEmail;
    private String studentRoll;
    private String punctual;
    private int clarity;
    private String engaging;
    private String pace;
    private int satisfaction;
    private String comments;
    private String submittedAt;

    // Constructors
    public FeedbackHistory() {}

    public FeedbackHistory(String subject, String courseCode, String faculty, String studentName,
                           String studentEmail, String studentRoll, String punctual, int clarity,
                           String engaging, String pace, int satisfaction, String comments) {
        this.subject = subject;
        this.courseCode = courseCode;
        this.faculty = faculty;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentRoll = studentRoll;
        this.punctual = punctual;
        this.clarity = clarity;
        this.engaging = engaging;
        this.pace = pace;
        this.satisfaction = satisfaction;
        this.comments = comments;
        this.submittedAt = java.time.LocalDateTime.now().toString();
    }

    // Getters and Setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentRoll() { return studentRoll; }
    public void setStudentRoll(String studentRoll) { this.studentRoll = studentRoll; }

    public String getPunctual() { return punctual; }
    public void setPunctual(String punctual) { this.punctual = punctual; }

    public int getClarity() { return clarity; }
    public void setClarity(int clarity) { this.clarity = clarity; }

    public String getEngaging() { return engaging; }
    public void setEngaging(String engaging) { this.engaging = engaging; }

    public String getPace() { return pace; }
    public void setPace(String pace) { this.pace = pace; }

    public int getSatisfaction() { return satisfaction; }
    public void setSatisfaction(int satisfaction) { this.satisfaction = satisfaction; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
}
