package com.serhiidiukarev.holiday.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A {@link LocalDateAdapter} implemented to serializer and
 * deserializer {@link LocalDate} for Json base on interval
 * representation of {@link DateTimeFormatter}
 */
public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Serialize date to Json
     *
     * @param localDate the object that needs to be converted to Json.
     * @param srcType   the actual type (fully genericized version) of the source object.
     * @return a tree of {@link JsonElement}s corresponding to the serialized form of {@code src}.
     */
    @Override
    public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDate));
    }

    /**
     * SDeserialize date from Json
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}