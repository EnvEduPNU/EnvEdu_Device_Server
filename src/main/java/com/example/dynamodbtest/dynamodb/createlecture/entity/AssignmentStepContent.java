package com.example.dynamodbtest.dynamodb.createlecture.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.example.dynamodbtest.dynamodb.createlecture.util.JsonConverter;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "AssignmentStepContent")
public class AssignmentStepContent {


    public AssignmentStepContent(String uuid, String timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    @DynamoDBHashKey
    private String uuid;

    @DynamoDBAttribute
    private String timestamp;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String stepName;

    @DynamoDBAttribute
    private int stepCount;

    @DynamoDBAttribute
    private List<ContentWrapper> contents;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @DynamoDBDocument
    public static class ContentWrapper {

        @DynamoDBAttribute
        private String contentName;

        @DynamoDBAttribute
        private int stepNum;

        @DynamoDBAttribute
        private List<Content> contents;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @DynamoDBDocument
    public static class Content {
        @DynamoDBAttribute
        private String type;

        @DynamoDBTypeConverted(converter = JsonConverter.class)
        @DynamoDBAttribute
        private Object content;

        @DynamoDBAttribute
        private int x;

        @DynamoDBAttribute
        private int y;
    }
}

