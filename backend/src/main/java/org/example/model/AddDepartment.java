package org.example.model;

import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;

public class AddDepartment {

    @BsonId
    private ObjectId id;  // MongoDB's native ObjectId

    private String departmentName;
    private String departmentType;
    private String className;
    private int semester;
    private List<AddDepartmentSubject> subjects;

    // Constructors
    public AddDepartment() {}

    public AddDepartment(String departmentName, String departmentType, String className, int semester, List<AddDepartmentSubject> subjects) {
        this.departmentName = departmentName;
        this.departmentType = departmentType;
        this.className = className;
        this.semester = semester;
        this.subjects = subjects;
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDepartmentType() { return departmentType; }
    public void setDepartmentType(String departmentType) { this.departmentType = departmentType; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public List<AddDepartmentSubject> getSubjects() { return subjects; }
    public void setSubjects(List<AddDepartmentSubject> subjects) { this.subjects = subjects; }
}
