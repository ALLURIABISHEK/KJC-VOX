package org.example.model;

public class SubjectResponse {
    private String courseCode;
    private String name;
    private String assignedFaculty;

    // Getters & Setters
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAssignedFaculty() { return assignedFaculty; }
    public void setAssignedFaculty(String assignedFaculty) { this.assignedFaculty = assignedFaculty; }
}
