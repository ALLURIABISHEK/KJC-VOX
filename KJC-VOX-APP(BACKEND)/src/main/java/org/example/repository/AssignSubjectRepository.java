package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.example.model.AssignSubject;

public class AssignSubjectRepository {

    private final MongoCollection<AssignSubject> collection;

    public AssignSubjectRepository(MongoDatabase database) {
        this.collection = database.getCollection("subject_assignments", AssignSubject.class);
    }

    public void save(AssignSubject assignSubject) {
        collection.insertOne(assignSubject);
    }

    public boolean isDuplicate(String faculty, String className, int semester, String subject) {
        return collection.find(
                Filters.and(
                        Filters.eq("faculty", faculty),
                        Filters.eq("className", className),
                        Filters.eq("semester", semester),
                        Filters.in("subjects", subject) // checks if subject is in the array
                )
        ).iterator().hasNext();
    }

    public AssignSubject findAssignedSubjects(String className, int semester, String facultyName) {
        return collection.find(
                Filters.and(
                        Filters.eq("className", className),
                        Filters.eq("semester", semester),
                        Filters.eq("faculty", facultyName)
                )
        ).first();
    }

    public AssignSubject findByFacultyClassAndSemester(String faculty, String className, int semester) {
        return collection.find(
                Filters.and(
                        Filters.eq("faculty", faculty),
                        Filters.eq("className", className),
                        Filters.eq("semester", semester)
                )
        ).first();
    }
}
