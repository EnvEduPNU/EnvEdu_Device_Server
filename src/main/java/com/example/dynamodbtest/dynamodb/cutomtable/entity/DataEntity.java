package com.example.dynamodbtest.dynamodb.cutomtable.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.example.dynamodbtest.dynamodb.cutomtable.converter.NumericFieldValueConverter;
import lombok.Getter;
import lombok.Setter;

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

    // numericFields에 커스텀 컨버터 적용
    @DynamoDBTypeConverted(converter = NumericFieldValueConverter.class)
    @DynamoDBAttribute(attributeName = "numericFields")
    private List<Map<String, Object>> numericFields;

    // stringFields는 DynamoDB JSON 지원을 통해 자동 직렬화
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "stringFields")
    private List<Map<String, StringFieldValue>> stringFields;

    @Getter
    @Setter
    public static class StringFieldValue {
        private String value;
        private int order;

        // StringFieldValue 생성자 - 문자열 값을 허용
        public StringFieldValue(String value, int order) {
            this.value = value;
            this.order = order;
        }
    }
}
