package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.model.AddDepartment;
import org.example.utils.MongoUtil;

import static com.mongodb.client.model.Filters.*;
import java.util.ArrayList;
import java.util.List;

public class AddDepartmentDAO {
    private final MongoCollection<AddDepartment> departmentCollection;

    public AddDepartmentDAO() {
        MongoDatabase db = MongoUtil.getDatabase();
        this.departmentCollection = db.getCollection("departments", AddDepartment.class);
    }
    public boolean insertOrUpdateDepartment(AddDepartment dept) {
        try {
            AddDepartment existing = getDepartmentByClassName(dept.getClassName());
            if (existing != null) {
                // Update existing
                departmentCollection.replaceOne(eq("className", dept.getClassName()), dept);
            } else {
                // Insert new
                departmentCollection.insertOne(dept);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AddDepartment> getAllDepartments() {
        return departmentCollection.find().into(new ArrayList<>());
    }

    public AddDepartment getDepartmentByClassName(String className) {
        return departmentCollection.find(eq("className", className)).first();
    }
    public List<AddDepartment> getBySemesterAndClass(int semester, String className) {
        return departmentCollection
                .find(and(eq("semester", semester), eq("className", className)))
                .into(new ArrayList<>());
    }

    public AddDepartment getByClassName(String className) {
        return departmentCollection.find(eq("className", className)).first();
    }

    public boolean deleteDepartmentByClassName(String className) {
        try {
            MongoDatabase database = MongoUtil.getDatabase();
            MongoCollection<AddDepartment> collection = database.getCollection("departments", AddDepartment.class);
            return collection.deleteOne(eq("className", className)).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean upsertDepartment(AddDepartment department) {
        try {
            AddDepartment existing = getByClassName(department.getClassName());

            if (existing != null) {
                // Update
                departmentCollection.replaceOne(eq("className", department.getClassName()), department);
            } else {
                // Insert
                departmentCollection.insertOne(department);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
