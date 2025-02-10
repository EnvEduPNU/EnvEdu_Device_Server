package com.example.dynamodbtest.dynamodb.log.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "LogCollection")
public class LogCollection {

    @DynamoDBHashKey
    private String logUuid;

    @DynamoDBAttribute
    private String eclassUuid;

    @DynamoDBAttribute
    private String eclassName;

    @DynamoDBAttribute
    private Integer stepNum;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String graphImage;

    @DynamoDBAttribute
    private String dataUUID;

    @DynamoDBAttribute
    private String logCollectionStartTime;

    @DynamoDBAttribute
    private String logCollectionEndTime;

    @DynamoDBAttribute
    private List<LogContent> content;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @DynamoDBDocument
    public static class LogContent {

        @DynamoDBAttribute
        private String logTime;

        @DynamoDBAttribute
        private String buttonName;

        @DynamoDBAttribute
        private Boolean actionSuccess;

        @DynamoDBAttribute
        private String memo;
    }
}
