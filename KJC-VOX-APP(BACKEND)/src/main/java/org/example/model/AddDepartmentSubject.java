package org.example.model;

public class AddDepartmentSubject {
    private String name;
    private String courseCode;

    public AddDepartmentSubject() {}

    public AddDepartmentSubject(String name, String courseCode) {
        this.name = name;
        this.courseCode = courseCode;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
}
