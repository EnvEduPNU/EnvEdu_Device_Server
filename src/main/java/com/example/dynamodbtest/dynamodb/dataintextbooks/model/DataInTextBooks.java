package com.example.dynamodbtest.dynamodb.dataintextbooks.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "DataInTextBooks")
public class DataInTextBooks {

    @DynamoDBHashKey(attributeName = "uuid")
    private String uuid;

    @DynamoDBAttribute(attributeName = "title")
    private String title;

    @DynamoDBAttribute(attributeName = "content")
    private String content;

    @DynamoDBAttribute(attributeName = "dataTypeLabel")
    private String dataTypeLabel;

    @DynamoDBAttribute(attributeName = "gradeLabel")
    private String gradeLabel;

    @DynamoDBAttribute(attributeName = "subjectLabel")
    private String subjectLabel;

    @DynamoDBAttribute(attributeName = "properties")
    private List<String> properties;

    @DynamoDBAttribute(attributeName = "data")
    private List<Map<String, String>> data; // 중첩된 리스트를 사용하여 시간과 예측 조위 데이터를 저장
}
