package org.example.model;

public class SemesterDistribution {
    private String semester;
    private int count;

    public SemesterDistribution() {}

    public SemesterDistribution(String semester, int count) {
        this.semester = semester;
        this.count = count;
    }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}