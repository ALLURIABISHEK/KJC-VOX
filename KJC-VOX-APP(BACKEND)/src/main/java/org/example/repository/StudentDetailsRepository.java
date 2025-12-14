package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.utils.MongoUtil;


public class StudentDetailsRepository {
    private final MongoCollection<Document> studentsCollection =
            MongoUtil.getDatabase().getCollection("MCA");

    public Document findByEmail(String email) {
        Bson query = Filters.eq("email", email.toLowerCase());
        return studentsCollection.find(query).first();
    }

    public boolean existsByEmail(String email) {
        Bson query = Filters.eq("email", email.toLowerCase());
        return studentsCollection.countDocuments(query) > 0;
    }
}