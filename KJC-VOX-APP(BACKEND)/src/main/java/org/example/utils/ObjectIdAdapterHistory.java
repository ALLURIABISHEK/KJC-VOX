package org.example.utils;

import com.google.gson.*;
import org.bson.types.ObjectId;
import java.lang.reflect.Type;

public class ObjectIdAdapterHistory implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {
    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toHexString());
    }

    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new ObjectId(json.getAsString());
    }
}
