package org.example.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.AssignSubject;
import org.example.model.SubjectResponse;
import org.example.repository.AssignSubjectRepository;
import org.example.utils.MongoUtil;

import java.util.ArrayList;
import java.util.List;

public class MySubjectsService {
    private final MongoDatabase database = MongoUtil.getDatabase();
    private final AssignSubjectRepository assignSubjectRepository;

    public MySubjectsService() {
        this.assignSubjectRepository = new AssignSubjectRepository(database); // ✅ Initialize repo
    }

    public List<SubjectResponse> getSubjectsForClass(String className, int semester) {
        MongoCollection<Document> departmentCollection = database.getCollection("departments");
        MongoCollection<Document> assignedCollection = database.getCollection("subject_assignments");

        Document query = new Document("className", className).append("semester", semester);
        Document department = departmentCollection.find(query).first();

        System.out.println("🔍 Searching in 'departments' with: " + query);

        List<SubjectResponse> result = new ArrayList<>();
        if (department == null || !department.containsKey("subjects")) {
            System.out.println("❌ No department or subjects found.");
            return result;
        }

        System.out.println("✅ Department found: " + department.toJson());

        List<Document> subjects = (List<Document>) department.get("subjects");
        List<Document> assignedList = assignedCollection
                .find(new Document("className", className).append("semester", semester))
                .into(new ArrayList<>());

        for (Document subjectDoc : subjects) {
            String subjectName = subjectDoc.getString("name").trim().toLowerCase();
            String assignedFaculty = "Not yet assigned";

            for (Document assigned : assignedList) {
                List<String> assignedSubjects = (List<String>) assigned.get("subjects");
                if (assignedSubjects != null) {
                    for (String sub : assignedSubjects) {
                        if (sub.trim().equalsIgnoreCase(subjectName)) {
                            assignedFaculty = assigned.getString("faculty");
                            break;
                        }
                    }
                }
                if (!assignedFaculty.equals("Not yet assigned")) break;
            }

            SubjectResponse subject = new SubjectResponse();
            subject.setCourseCode(subjectDoc.getString("courseCode"));
            subject.setName(subjectDoc.getString("name"));
            subject.setAssignedFaculty(assignedFaculty);

            result.add(subject);
        }

        return result;
    }

    // ✅ This uses your repository now
    public AssignSubject getSubjectsForFaculty(String faculty, String className, int semester) {
        return assignSubjectRepository.findByFacultyClassAndSemester(faculty, className, semester);
    }
}
