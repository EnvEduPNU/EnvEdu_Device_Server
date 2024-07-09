package com.example.dynamodbtest.dynamodb.createlecture.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter implements DynamoDBTypeConverter<String, Object> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convert(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert object to JSON string", e);
        }
    }

    @Override
    public Object unconvert(String string) {
        try {
            return OBJECT_MAPPER.readValue(string, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert JSON string to object", e);
        }
    }
}
