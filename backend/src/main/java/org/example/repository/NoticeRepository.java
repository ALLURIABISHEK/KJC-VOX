package org.example.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.model.Notice;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NoticeRepository {
    private final MongoCollection<Document> collection;
    private final Gson gson = new Gson();

    public NoticeRepository(MongoDatabase database) {
        this.collection = database.getCollection("notices");
    }

    public void saveNotice(Notice notice) {
        Document doc = Document.parse(gson.toJson(notice));
        collection.insertOne(doc);
    }

    public List<Notice> getNoticesFor(String noticeFor) {
        List<Notice> list = new ArrayList<>();
        FindIterable<Document> docs = collection.find(
                Filters.or(
                        Filters.eq("noticeFor", noticeFor),
                        Filters.eq("noticeFor", "both") // include general notices
                )
        );
        for (Document doc : docs) {
            list.add(gson.fromJson(doc.toJson(), Notice.class));
        }
        return list;
    }


}
