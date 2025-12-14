package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class TestSubRepository {
    private final MongoCollection<Document> subjectAssignmentsCollection;

    public TestSubRepository(MongoDatabase database) {
        this.subjectAssignmentsCollection = database.getCollection("subject_assignments");
    }

    // Returns ALL matching assignments for a faculty, class, and semester
    public List<Document> findAllAssignmentsByFacultyClassSemester(String faculty, String className, int semester) {
        List<Document> assignments = new ArrayList<>();

        try {
            Document query = new Document()
                    .append("faculty", faculty)
                    .append("className", className)
                    .append("semester", semester);

            System.out.println("🔍🔍🔍 TestSubRepository: Querying subject_assignments collection");
            System.out.println("📋 Query: " + query.toJson());

            // Get ALL matching documents
            FindIterable<Document> results = subjectAssignmentsCollection.find(query);

            int count = 0;
            for (Document doc : results) {
                count++;
                assignments.add(doc);
                System.out.println("✅ Found assignment #" + count + ": ID=" + doc.get("_id"));
                System.out.println("   Subjects in this doc: " + doc.get("subjects"));
            }

            if (assignments.isEmpty()) {
                System.out.println("❌❌❌ NO assignments found for query: " + query.toJson());

                // Debug: Check if collection has any data
                long totalDocs = subjectAssignmentsCollection.countDocuments();
                System.out.println("⚠️ Total documents in subject_assignments collection: " + totalDocs);

                if (totalDocs > 0) {
                    System.out.println("⚠️ Collection has data, but query returned nothing. Check parameter values:");
                    System.out.println("   Faculty: '" + faculty + "' (length: " + faculty.length() + ")");
                    System.out.println("   ClassName: '" + className + "' (length: " + className.length() + ")");
                    System.out.println("   Semester: " + semester);
                }
            } else {
                System.out.println("✅✅✅ Total assignments found: " + assignments.size());
            }

        } catch (Exception e) {
            System.err.println("❌ Error in TestSubRepository: " + e.getMessage());
            e.printStackTrace();
        }

        return assignments;
    }
}