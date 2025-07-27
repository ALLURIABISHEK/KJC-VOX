package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestSubRepository {
    private final MongoCollection<Document> subjectAssignmentsCollection;

    public TestSubRepository(MongoDatabase database) {
        this.subjectAssignmentsCollection = database.getCollection("subject_assignments");
    }

    public Document findAssignmentByFacultyClassSemester(String faculty, String className, int semester) {
        try {
            Document query = new Document()
                    .append("faculty", faculty)
                    .append("className", className)
                    .append("semester", semester);

            System.out.println("🔍 TestSubRepository: Querying with: " + query.toJson());

            Document result = subjectAssignmentsCollection.find(query).first();

            if (result != null) {
                System.out.println("✅ Found assignment: " + result.toJson());
            } else {
                System.out.println("❌ No assignment found for query: " + query.toJson());
            }

            return result;

        } catch (Exception e) {
            System.err.println("❌ Error in TestSubRepository: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}