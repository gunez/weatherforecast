package com.finleap.weatherforecast.api.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finleap.weatherforecast.exceptions.InternalServiceException;

import java.io.IOException;

public class Misc {

    private Misc() { }

    public static String serializeSafeJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);

        } catch (JsonProcessingException e) {
            throw new InternalServiceException("Failed to serialize json object!", e);
        }

    }

    public static <T> T deserializeSafeJson(String json, Class<T> theClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, theClass);
        } catch (IOException e) {
            throw new InternalServiceException("Failed to deserialize json object!", e);
        }
    }
}
