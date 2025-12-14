package org.example.model;

public class DepartmentOverview {
    private String departmentName;
    private int studentCount;
    private int facultyCount;
    private int subjectCount;

    public DepartmentOverview() {}

    public DepartmentOverview(String departmentName, int studentCount,
                              int facultyCount, int subjectCount) {
        this.departmentName = departmentName;
        this.studentCount = studentCount;
        this.facultyCount = facultyCount;
        this.subjectCount = subjectCount;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getFacultyCount() {
        return facultyCount;
    }

    public void setFacultyCount(int facultyCount) {
        this.facultyCount = facultyCount;
    }

    public int getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(int subjectCount) {
        this.subjectCount = subjectCount;
    }
}