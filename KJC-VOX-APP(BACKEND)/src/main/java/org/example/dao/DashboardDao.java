package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.*;
import org.example.utils.MongoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class DashboardDao {

    private MongoDatabase database;

    public DashboardDao() {
        this.database = MongoUtil.getDatabase();
    }

    /**
     * Get total student count from MCA collection
     */
    public long getTotalStudents() {
        MongoCollection<Document> collection = database.getCollection("MCA");
        return collection.countDocuments();
    }

    /**
     * Get total faculty count
     */
    public long getTotalFaculty() {
        MongoCollection<Document> collection = database.getCollection("faculty");
        return collection.countDocuments();
    }

    /**
     * Get total department count
     */
    public long getTotalDepartments() {
        MongoCollection<Document> collection = database.getCollection("departments");
        return collection.countDocuments();
    }

    /**
     * Get total subjects count across all departments
     */
    public long getTotalSubjects() {
        MongoCollection<Document> collection = database.getCollection("departments");
        long totalSubjects = 0;

        for (Document dept : collection.find()) {
            List<?> subjects = dept.getList("subjects", Document.class);
            if (subjects != null) {
                totalSubjects += subjects.size();
            }
        }

        return totalSubjects;
    }

    /**
     * Get total notices count
     */
    public long getTotalNotices() {
        MongoCollection<Document> collection = database.getCollection("notices");
        return collection.countDocuments();
    }

    /**
     * Get total feedbacks count
     */
    public long getTotalFeedbacks() {
        MongoCollection<Document> collection = database.getCollection("feedbacks");
        return collection.countDocuments();
    }

    /**
     * Get student distribution grouped by class
     */
    public List<StudentDistribution> getStudentDistributionByClass() {
        MongoCollection<Document> collection = database.getCollection("MCA");
        List<StudentDistribution> distribution = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$class")
                        .append("count", new Document("$sum", 1))),
                new Document("$sort", new Document("_id", 1))
        );

        for (Document doc : collection.aggregate(pipeline)) {
            String className = doc.getString("_id");
            int count = doc.getInteger("count");
            distribution.add(new StudentDistribution(className, count));
        }

        return distribution;
    }

    /**
     * Get faculty distribution by department type (PG/UG)
     */
    public List<FacultyDistribution> getFacultyDistributionByType() {
        MongoCollection<Document> collection = database.getCollection("faculty");
        List<FacultyDistribution> distribution = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$departmentType")
                        .append("count", new Document("$sum", 1))),
                new Document("$sort", new Document("_id", 1))
        );

        for (Document doc : collection.aggregate(pipeline)) {
            String deptType = doc.getString("_id");
            int count = doc.getInteger("count");
            distribution.add(new FacultyDistribution(deptType, count));
        }

        return distribution;
    }

    /**
     * Get student distribution by semester
     */
    public List<SemesterDistribution> getSemesterDistribution() {
        MongoCollection<Document> collection = database.getCollection("MCA");
        List<SemesterDistribution> distribution = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$semester")
                        .append("count", new Document("$sum", 1))),
                new Document("$sort", new Document("_id", 1))
        );

        for (Document doc : collection.aggregate(pipeline)) {
            String semester = doc.getString("_id");
            int count = doc.getInteger("count");
            distribution.add(new SemesterDistribution(semester, count));
        }

        return distribution;
    }
    /**
     * Get department overview with students, faculty, and subjects count
     */
    public List<DepartmentOverview> getDepartmentOverview() {
        List<DepartmentOverview> overview = new ArrayList<>();

        MongoCollection<Document> deptCollection = database.getCollection("departments");
        MongoCollection<Document> studentCollection = database.getCollection("MCA");
        MongoCollection<Document> facultyCollection = database.getCollection("faculty");

        for (Document dept : deptCollection.find()) {
            String deptName = dept.getString("departmentName");
            String deptType = dept.getString("departmentType");

            // Count subjects
            List<?> subjects = dept.getList("subjects", Document.class);
            int subjectCount = subjects != null ? subjects.size() : 0;

            // Count students in this department
            long studentCount = studentCollection.countDocuments(
                    new Document("department", deptName)
            );

            // Count faculty in this department type
            long facultyCount = facultyCollection.countDocuments(
                    new Document("departmentType", deptType)
            );

            overview.add(new DepartmentOverview(
                    deptName + " [" + deptType + "]",
                    (int) studentCount,
                    (int) facultyCount,
                    subjectCount
            ));
        }

        return overview;
    }
}