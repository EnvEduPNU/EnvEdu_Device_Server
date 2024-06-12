package com.example.dynamodbtest.dynamodb.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.util.Map;

@DynamoDBTable(tableName = "DynamicData")
public class DynamicData {
    private String id;
    private Map<String, Object> additionalFields;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "additionalFields")
    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
    }
}

