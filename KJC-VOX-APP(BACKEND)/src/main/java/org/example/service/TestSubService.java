package org.example.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.TestSubResponse;
import org.example.repository.TestSubRepository;
import org.example.utils.MongoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSubService {
    private final MongoDatabase database = MongoUtil.getDatabase();
    private final TestSubRepository testSubRepository;

    public TestSubService() {
        this.testSubRepository = new TestSubRepository(database);
    }

    public List<TestSubResponse> getSubjectsForFaculty(String faculty, String className, int semester) {
        System.out.println("🔍 TestSubService: Searching for faculty: " + faculty + ", class: " + className + ", semester: " + semester);

        List<TestSubResponse> result = new ArrayList<>();
        Set<String> addedSubjects = new HashSet<>(); // To avoid duplicates

        try {
            // Get ALL assignments for this faculty, class, and semester
            List<Document> assignments = testSubRepository.findAllAssignmentsByFacultyClassSemester(faculty, className, semester);

            if (assignments.isEmpty()) {
                System.out.println("❌ No assignments found for faculty: " + faculty);
                return result;
            }

            System.out.println("✅ Found " + assignments.size() + " assignment document(s)");

            // Get subject details from departments collection
            MongoCollection<Document> departmentCollection = database.getCollection("departments");
            Document departmentQuery = new Document("className", className).append("semester", semester);

            System.out.println("🔍 Querying departments with: " + departmentQuery.toJson());

            Document department = departmentCollection.find(departmentQuery).first();

            List<Document> allSubjects = null;

            if (department != null && department.containsKey("subjects")) {
                allSubjects = (List<Document>) department.get("subjects");
                System.out.println("✅ Found " + allSubjects.size() + " subjects in departments collection");
            } else {
                System.out.println("⚠️ No department found, will use subject names from assignments directly");
            }

            // Process ALL assignments
            for (Document assignment : assignments) {
                System.out.println("📋 Processing assignment: " + assignment.get("_id"));

                List<String> assignedSubjects = (List<String>) assignment.get("subjects");

                if (assignedSubjects == null || assignedSubjects.isEmpty()) {
                    System.out.println("⚠️ No subjects found in this assignment");
                    continue;
                }

                System.out.println("📚 Assignment contains " + assignedSubjects.size() + " subject(s): " + assignedSubjects);

                // Process each subject in this assignment
                for (String assignedSubject : assignedSubjects) {
                    String trimmedAssignedSubject = assignedSubject.trim();
                    String lowerCaseSubject = trimmedAssignedSubject.toLowerCase();

                    // Skip if we've already added this subject (avoid duplicates)
                    if (addedSubjects.contains(lowerCaseSubject)) {
                        System.out.println("⚠️ Subject '" + assignedSubject + "' already added, skipping duplicate");
                        continue;
                    }

                    TestSubResponse subject = new TestSubResponse();
                    boolean subjectFound = false;

                    // Try to find matching subject in departments collection
                    if (allSubjects != null) {
                        for (Document subjectDoc : allSubjects) {
                            String subjectName = subjectDoc.getString("name");
                            if (subjectName != null && subjectName.trim().equalsIgnoreCase(trimmedAssignedSubject)) {
                                subject.setCourseCode(subjectDoc.getString("courseCode"));
                                subject.setName(subjectDoc.getString("name"));
                                subject.setAssignedFaculty(faculty);
                                subjectFound = true;
                                System.out.println("✅ Matched subject with departments: " + subject.getName());
                                break;
                            }
                        }
                    }

                    // If not found in departments, use the subject name directly
                    if (!subjectFound) {
                        subject.setName(trimmedAssignedSubject);
                        subject.setCourseCode("N/A"); // Default course code
                        subject.setAssignedFaculty(faculty);
                        System.out.println("⚠️ Subject not found in departments, using direct name: " + trimmedAssignedSubject);
                    }

                    result.add(subject);
                    addedSubjects.add(lowerCaseSubject);
                    System.out.println("✅ Added subject #" + result.size() + ": " + subject.getName() + " (code: " + subject.getCourseCode() + ")");
                }
            }

            System.out.println("✅✅✅ TOTAL unique subjects returned for faculty " + faculty + ": " + result.size());
            System.out.println("📋 Subject names: " + addedSubjects);

        } catch (Exception e) {
            System.err.println("❌ Error in TestSubService: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}