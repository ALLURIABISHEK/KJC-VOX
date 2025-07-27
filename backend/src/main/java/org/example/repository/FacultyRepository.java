package org.example.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.model.Faculty;
import org.example.utils.MongoUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;


public class FacultyRepository {
    private static final String COLLECTION_NAME = "faculty";

    private final Gson gson = new Gson();


    private MongoCollection<Faculty> getCollection() {
        return MongoUtil.getDatabase().getCollection(COLLECTION_NAME, Faculty.class);
    }

    private boolean isValidObjectId(String id) {
        return id != null && id.matches("[0-9a-fA-F]{24}");
    }

    public Faculty save(Faculty faculty) {
        try {
            System.out.println("=== REPOSITORY save called ===");
            System.out.println("Faculty to save: " + faculty);

            MongoCollection<Faculty> collection = getCollection();
            System.out.println("Got collection: " + collection.getNamespace());

            InsertOneResult result = collection.insertOne(faculty);
            System.out.println("Insert result: " + result);
            System.out.println("Was acknowledged: " + result.wasAcknowledged());

            if (result.wasAcknowledged()) {
                faculty.setId(result.getInsertedId().asObjectId().getValue());
                System.out.println("Saved faculty with ID: " + faculty.getId());
                return faculty;
            }

            System.out.println("Insert was not acknowledged!");
            return null;
        } catch (Exception e) {
            System.err.println("=== REPOSITORY ERROR ===");
            System.err.println("Error saving faculty: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save faculty", e);
        }
    }
    public List<Faculty> findAll() {
        List<Faculty> facultyList = new ArrayList<>();
        try (MongoCursor<Faculty> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Faculty faculty = cursor.next();
                System.out.println("Retrieved faculty: " + faculty);
                facultyList.add(faculty);
            }
            return facultyList;
        } catch (Exception e) {
            System.err.println("Error finding all faculty: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve faculty", e);
        }
    }

    public Faculty findById(String id) {
        try {
            if (!isValidObjectId(id)) {
                throw new IllegalArgumentException("Invalid ObjectId format");
            }
            Faculty faculty = getCollection().find(Filters.eq("_id", new ObjectId(id))).first();
            System.out.println("Find by ID result: " + (faculty != null ? faculty.debugLog() : "null"));
            return faculty;
        } catch (Exception e) {
            System.err.println("Error finding faculty by ID: " + e.getMessage());
            throw new RuntimeException("Failed to find faculty by ID", e);
        }
    }

    public Faculty findByFacultyId(String facultyId) {
        try {
            Faculty faculty = getCollection().find(Filters.eq("facultyId", facultyId)).first();
            System.out.println("Find by FacultyID result: " + (faculty != null ? faculty.debugLog() : "null"));
            return faculty;
        } catch (Exception e) {
            System.err.println("Error finding faculty by FacultyID: " + e.getMessage());
            throw new RuntimeException("Failed to find faculty by FacultyID", e);
        }
    }
    public boolean deleteByEmail(String email) {
        try {
            DeleteResult result = getCollection().deleteOne(Filters.eq("email", email));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting faculty by email: " + e.getMessage());
            throw new RuntimeException("Failed to delete faculty by email", e);
        }
    }
    public Faculty updateByEmail(String email, Faculty updatedData) {
        MongoCollection<Document> collection = MongoUtil.getDatabase().getCollection(COLLECTION_NAME);

        Document existing = collection.find(Filters.eq("email", email)).first();

        if (existing == null) {
            return null; // not found
        }

        // 🔐 Do NOT allow facultyId change — keep the original one
        updatedData.setFacultyId(existing.getString("facultyId")); // this is the key line ✅

        Document updateDoc = new Document()
                .append("facultyId", updatedData.getFacultyId()) // keep original
                .append("fullName", updatedData.getFullName())
                .append("email", updatedData.getEmail())
                .append("department", updatedData.getDepartment())
                .append("joiningDate", updatedData.getJoiningDate())
                .append("departmentType", updatedData.getDepartmentType());

        Document result = collection.findOneAndUpdate(
                Filters.eq("email", email),
                new Document("$set", updateDoc),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );

        return result != null ? gson.fromJson(result.toJson(), Faculty.class) : null;
    }



    public boolean delete(String id) {
        try {
            if (!isValidObjectId(id)) {
                throw new IllegalArgumentException("Invalid ObjectId format");
            }
            DeleteResult result = getCollection().deleteOne(Filters.eq("_id", new ObjectId(id)));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting faculty: " + e.getMessage());
            throw new RuntimeException("Failed to delete faculty", e);
        }
    }

    public List<Faculty> search(String fullName, String email, String departmentType,
                                String fromDate, String toDate) {
        List<Faculty> facultyList = new ArrayList<>();
        try {
            List<Bson> filters = new ArrayList<>();

            if (fullName != null && !fullName.trim().isEmpty()) {
                filters.add(Filters.regex("fullName", Pattern.compile(fullName, Pattern.CASE_INSENSITIVE)));
            }
            if (email != null && !email.trim().isEmpty()) {
                filters.add(Filters.regex("email", Pattern.compile(email, Pattern.CASE_INSENSITIVE)));
            }
            if (departmentType != null && !departmentType.trim().isEmpty()) {
                filters.add(Filters.eq("departmentType", departmentType));
            }
            if (fromDate != null && !fromDate.trim().isEmpty()) {
                filters.add(Filters.gte("joiningDate", fromDate));
            }
            if (toDate != null && !toDate.trim().isEmpty()) {
                filters.add(Filters.lte("joiningDate", toDate));
            }

            Bson query = filters.isEmpty() ? new Document() : Filters.and(filters);
            try (MongoCursor<Faculty> cursor = getCollection().find(query).iterator()) {
                while (cursor.hasNext()) {
                    facultyList.add(cursor.next());
                }
            }
            return facultyList;
        } catch (Exception e) {
            System.err.println("Error searching faculty: " + e.getMessage());
            throw new RuntimeException("Failed to search faculty", e);
        }
    }

    public Faculty getFacultyByEmail(String email) {
        MongoCollection<Document> collection = MongoUtil.getDatabase().getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Faculty.class) : null;
    }


    public Faculty findByEmail(String email) {
        return getCollection().find(eq("email", email.toLowerCase())).first();
    }


}
