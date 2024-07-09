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
@DynamoDBTable(tableName = "StepContent")
public class StepContent {

    @DynamoDBHashKey
    private String stepName;

    @DynamoDBAttribute
    private List<ContentWrapper> contents;

    @DynamoDBAttribute
    private int stepCount;

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
    }
}

