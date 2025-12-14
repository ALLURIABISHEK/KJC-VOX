package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.example.model.Faculty;
import org.example.utils.MongoUtil;

public class RemovedFacultyRepository {
    private static final String COLLECTION_NAME = "removed_faculty";

    private MongoCollection<Faculty> getCollection() {
        return MongoUtil.getDatabase().getCollection(COLLECTION_NAME, Faculty.class);
    }

    public void saveRemovedFaculty(Faculty faculty) {
        try {
            InsertOneResult result = getCollection().insertOne(faculty);
            System.out.println("✅ Removed faculty stored: " + result.getInsertedId());
        } catch (Exception e) {
            System.err.println("Error saving removed faculty: " + e.getMessage());
        }
    }

    public long getRemovedFacultyCount() {
        return getCollection().countDocuments();
    }
}
