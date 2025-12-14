package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.utils.MongoUtil;

import java.util.Date;

public class ResetPasswordRepository {
    private final MongoCollection<Document> mcaCollection =
            MongoUtil.getDatabase().getCollection("MCA");
    private final MongoCollection<Document> userCollection =
            MongoUtil.getDatabase().getCollection("student-U-login");

    public Document findStudentByRollNo(String rollNo) {
        Bson query = Filters.eq("rollnumber", rollNo.toLowerCase());
        return mcaCollection.find(query).first();
    }

    public boolean isUserRegistered(String email) {
        Bson query = Filters.eq("email", email.toLowerCase());
        return userCollection.find(query).first() != null;
    }

    public boolean updatePassword(String email, String newHashedPassword) {
        Bson query = Filters.eq("email", email.toLowerCase());
        Bson update = Updates.combine(
                Updates.set("password", newHashedPassword),
                Updates.set("temporaryPassword", false),
                Updates.set("lastPasswordChange", new Date())
        );
        return userCollection.updateOne(query, update).getModifiedCount() > 0;
    }
}