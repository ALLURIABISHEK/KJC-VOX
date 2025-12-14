package org.example.model;

public class FacultyDistribution {
    private String departmentType;
    private int count;

    public FacultyDistribution() {}

    public FacultyDistribution(String departmentType, int count) {
        this.departmentType = departmentType;
        this.count = count;
    }

    public String getDepartmentType() { return departmentType; }
    public void setDepartmentType(String departmentType) { this.departmentType = departmentType; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}