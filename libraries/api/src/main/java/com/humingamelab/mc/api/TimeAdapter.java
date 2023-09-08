package com.humingamelab.mc.api;

import com.apollographql.apollo3.api.Adapter;
import com.apollographql.apollo3.api.CustomScalarAdapters;
import com.apollographql.apollo3.api.json.JsonReader;
import com.apollographql.apollo3.api.json.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;

public class TimeAdapter implements Adapter<ZonedDateTime> {
        @Override
        public ZonedDateTime fromJson(JsonReader reader, CustomScalarAdapters customScalarAdapters) throws IOException {
            String date = reader.nextString();
            return ZonedDateTime.parse(date);
        }

        @Override
        public void toJson(JsonWriter writer, CustomScalarAdapters customScalarAdapters, ZonedDateTime value) throws IOException {
            writer.value(value.toString());
        }

}
