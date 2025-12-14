package org.example.model;

public class TestSubResponse {
    private String courseCode;
    private String name;
    private String assignedFaculty;

    public TestSubResponse() {
    }

    public TestSubResponse(String courseCode, String name, String assignedFaculty) {
        this.courseCode = courseCode;
        this.name = name;
        this.assignedFaculty = assignedFaculty;
    }

    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignedFaculty() {
        return assignedFaculty;
    }

    public void setAssignedFaculty(String assignedFaculty) {
        this.assignedFaculty = assignedFaculty;
    }

    @Override
    public String toString() {
        return "TestSubResponse{" +
                "courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", assignedFaculty='" + assignedFaculty + '\'' +
                '}';
    }
}