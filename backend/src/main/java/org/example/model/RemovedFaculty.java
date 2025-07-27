// File: org.example.model.RemovedFaculty.java
package org.example.model;

import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonId;

public class RemovedFaculty {

    @BsonId
    private ObjectId id;

    private String email;
    private String fullName;
    private String removalDate;

    public RemovedFaculty() {}

    public RemovedFaculty(String email, String fullName, String removalDate) {
        this.email = email;
        this.fullName = fullName;
        this.removalDate = removalDate;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRemovalDate() { return removalDate; }
    public void setRemovalDate(String removalDate) { this.removalDate = removalDate; }
}
