package org.example.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.TestSubResponse;
import org.example.repository.TestSubRepository;
import org.example.utils.MongoUtil;

import java.util.ArrayList;
import java.util.List;

public class TestSubService {
    private final MongoDatabase database = MongoUtil.getDatabase();
    private final TestSubRepository testSubRepository;

    public TestSubService() {
        this.testSubRepository = new TestSubRepository(database);
    }

    public List<TestSubResponse> getSubjectsForFaculty(String faculty, String className, int semester) {
        System.out.println("🔍 TestSubService: Searching for faculty: " + faculty + ", class: " + className + ", semester: " + semester);

        List<TestSubResponse> result = new ArrayList<>();

        try {
            // Get assignment for this specific faculty, class, and semester
            Document assignment = testSubRepository.findAssignmentByFacultyClassSemester(faculty, className, semester);

            if (assignment == null) {
                System.out.println("❌ No assignment found for faculty: " + faculty);
                return result;
            }

            System.out.println("✅ Assignment found: " + assignment.toJson());

            // Get the subjects assigned to this faculty
            List<String> assignedSubjects = (List<String>) assignment.get("subjects");

            if (assignedSubjects == null || assignedSubjects.isEmpty()) {
                System.out.println("❌ No subjects found in assignment");
                return result;
            }

            // Get subject details from departments collection
            MongoCollection<Document> departmentCollection = database.getCollection("departments");
            Document departmentQuery = new Document("className", className).append("semester", semester);
            Document department = departmentCollection.find(departmentQuery).first();

            if (department == null || !department.containsKey("subjects")) {
                System.out.println("❌ No department or subjects found in departments collection");
                return result;
            }

            List<Document> allSubjects = (List<Document>) department.get("subjects");

            // Filter subjects to only include those assigned to this faculty
            for (String assignedSubject : assignedSubjects) {
                String trimmedAssignedSubject = assignedSubject.trim().toLowerCase();

                for (Document subjectDoc : allSubjects) {
                    String subjectName = subjectDoc.getString("name");
                    if (subjectName != null && subjectName.trim().toLowerCase().equals(trimmedAssignedSubject)) {
                        TestSubResponse subject = new TestSubResponse();
                        subject.setCourseCode(subjectDoc.getString("courseCode"));
                        subject.setName(subjectDoc.getString("name"));
                        subject.setAssignedFaculty(faculty); // This faculty is the one assigned

                        result.add(subject);
                        System.out.println("✅ Added subject: " + subject.getName() + " for faculty: " + faculty);
                        break;
                    }
                }
            }

            System.out.println("✅ Total subjects found for faculty " + faculty + ": " + result.size());

        } catch (Exception e) {
            System.err.println("❌ Error in TestSubService: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}