package com.humingamelab.mc.api;

import com.apollographql.apollo3.api.Adapter;
import com.apollographql.apollo3.api.CustomScalarAdapters;
import com.apollographql.apollo3.api.json.JsonReader;
import com.apollographql.apollo3.api.json.JsonWriter;

import java.io.IOException;
import java.util.HashMap;

public class MapAdapter implements Adapter<HashMap> {
    @Override
    public HashMap fromJson(JsonReader reader, CustomScalarAdapters customScalarAdapters) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull();
            return null;
        }

        HashMap map = new HashMap();
        reader.beginObject();

        while (reader.hasNext()) {
            String key = reader.nextName();
            String value = reader.nextString();
            map.put(key, value);
        }

        reader.endObject();
        return map;
    }

    @Override
    public void toJson(JsonWriter writer, CustomScalarAdapters customScalarAdapters, HashMap value) throws IOException {
        writer.beginObject();
        for (Object key : value.keySet()) {
            writer.name(key.toString());
            if (value.get(key) instanceof Integer) {
                writer.value((Integer) value.get(key));
                continue;
            }
            writer.value(value.get(key).toString());
        }
        writer.endObject();
    }

}
