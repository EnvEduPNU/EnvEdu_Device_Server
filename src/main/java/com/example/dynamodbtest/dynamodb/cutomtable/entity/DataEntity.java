package com.example.dynamodbtest.dynamodb.cutomtable.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import com.example.dynamodbtest.dynamodb.cutomtable.dto.DataDTO;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.endpoints.internal.Value;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@DynamoDBTable(tableName = "CustomData")
public class DataEntity {

    @DynamoDBHashKey(attributeName = "dataUUID")
    private String dataUUID;

    @DynamoDBAttribute(attributeName = "saveDate")
    private String saveDate;

    @DynamoDBAttribute(attributeName = "memo")
    private String memo;

    @DynamoDBAttribute(attributeName = "dataLabel")
    private String dataLabel;

    @DynamoDBAttribute(attributeName = "userName")
    private String userName;

    // 각 필드는 value와 order를 포함하는 구조로 변경
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "numericFields")
    private List<Map<String, FieldValue>> numericFields;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "stringFields")
    private List<Map<String, FieldValue>> stringFields;

    @Getter
    @Setter
    public static class FieldValue {
        private String value;
        private int order;

        // FieldValue의 생성자
        public FieldValue(String value, int order) {
            this.value = value;
            this.order = order;
        }
    }
}
