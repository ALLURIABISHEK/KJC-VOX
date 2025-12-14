package org.example.model;

public class StudentDistribution {
    private String className;
    private int count;

    public StudentDistribution() {}

    public StudentDistribution(String className, int count) {
        this.className = className;
        this.count = count;
    }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}