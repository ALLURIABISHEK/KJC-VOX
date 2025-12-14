package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.model.User;
import org.example.utils.MongoUtil;

import java.util.Date;

public class UserRepository {
    private final MongoCollection<Document> usersCollection =
            MongoUtil.getDatabase().getCollection("student-U-login");

    public void saveUser(String name, String email, String hashedPassword) {
        Document doc = new Document()
                .append("name", name)
                .append("email", email.toLowerCase())
                .append("password", hashedPassword)
                .append("createdAt", new Date())
                .append("isVerified", false) // Initially false
                .append("temporaryPassword", true);
        usersCollection.insertOne(doc);
    }

    public User findByEmail(String email) {
        Bson query = Filters.eq("email", email.toLowerCase());
        Document doc = usersCollection.find(query).first();
        if (doc == null) return null;

        return new User(
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("password"),
                doc.getBoolean("temporaryPassword", false),
                doc.getDate("createdAt"),
                doc.getBoolean("isVerified", false)
        );
    }

    public boolean markAsVerified(String email) {
        Bson query = Filters.eq("email", email.toLowerCase());
        Bson update = new Document("$set",
                new Document("isVerified", true)
                        .append("verifiedAt", new Date()));
        return usersCollection.updateOne(query, update).getModifiedCount() > 0;
    }
}