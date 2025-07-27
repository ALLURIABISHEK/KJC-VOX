package org.example.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.configuration.CodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb+srv://24mcab07:RadhaBhaskar%401@kjc-vox-cluster.ps4pwaz.mongodb.net/kjc_vox_portal?retryWrites=true&w=majority&appName=KJC-VOX-Cluster";
    private static final String DB_NAME = "kjc_vox_portal";
    private static MongoClient client;
    private static MongoDatabase database;

    static {
        try {
            // Create custom codec provider
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                    .register("org.example.model")
                    .automatic(true) // <-- required
                    .build();

            // Combine with default codec registry
            CodecRegistry codecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(pojoCodecProvider)
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .codecRegistry(codecRegistry)
                    .build();

            client = MongoClients.create(settings);
            database = client.getDatabase(DB_NAME);

            System.out.println("MongoDB connection established successfully");

        } catch (Exception e) {
            System.err.println("Failed to initialize MongoDB connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize MongoDB connection", e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new RuntimeException("Database not initialized. Check MongoDB connection.");
        }
        return database;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}