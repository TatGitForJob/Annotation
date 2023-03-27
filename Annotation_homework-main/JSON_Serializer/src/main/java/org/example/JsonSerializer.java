package org.example;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class JsonSerializer<T> {
    private final Set<Field> publishedFields;

    public JsonSerializer(Class<T> serializedClass) {
        publishedFields = new HashSet<>();
        for (Field field : serializedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Published.class)) {
                publishedFields.add(field);
            }
        }
    }

    public JSONObject serialize(T o) {
        JSONObject result = new JSONObject();
        for (Field field : publishedFields) {
            try {
                field.setAccessible(true);
                result.put(field.getName(), field.get(o).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
