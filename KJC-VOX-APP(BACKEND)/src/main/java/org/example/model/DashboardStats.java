package org.example.model;

public class DashboardStats {
    private long totalStudents;
    private long totalFaculty;
    private long totalDepartments;
    private long totalSubjects;
    private long totalNotices;
    private long totalFeedbacks;

    public DashboardStats() {}

    public DashboardStats(long totalStudents, long totalFaculty, long totalDepartments,
                          long totalSubjects, long totalNotices, long totalFeedbacks) {
        this.totalStudents = totalStudents;
        this.totalFaculty = totalFaculty;
        this.totalDepartments = totalDepartments;
        this.totalSubjects = totalSubjects;
        this.totalNotices = totalNotices;
        this.totalFeedbacks = totalFeedbacks;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalFaculty() {
        return totalFaculty;
    }

    public void setTotalFaculty(long totalFaculty) {
        this.totalFaculty = totalFaculty;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public long getTotalSubjects() {
        return totalSubjects;
    }

    public void setTotalSubjects(long totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public long getTotalNotices() {
        return totalNotices;
    }

    public void setTotalNotices(long totalNotices) {
        this.totalNotices = totalNotices;
    }

    public long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public void setTotalFeedbacks(long totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }
}