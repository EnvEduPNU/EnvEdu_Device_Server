package com.example.dynamodbtest.dynamodb.cutomtable.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DataDTO {

    private String dataUUID;
    private String saveDate;
    private String title;
    private String memo;
    private String dataLabel;
    private String userName;

    // NumericFieldValueConverter를 사용하여 JSON으로 직렬화/역직렬화
    private List<Map<String, NumericFieldValue>> numericFields;

    private List<Map<String, StringFieldValue>> stringFields;

    // 생성자
    public DataDTO(String dataUUID, String saveDate, String title, String memo, String dataLabel, String userName,
                   List<Map<String, NumericFieldValue>> numericFields, List<Map<String, StringFieldValue>> stringFields) {
        this.dataUUID = dataUUID;
        this.saveDate = saveDate;
        this.title = title;
        this.memo = memo;
        this.dataLabel = dataLabel;
        this.userName = userName;
        this.numericFields = numericFields;
        this.stringFields = stringFields;
    }

    @Getter
    @Setter
    public static class NumericFieldValue {
        private Object value; // int 또는 double 값을 저장할 수 있는 Number 타입
        private int order;

        // NumericFieldValue 생성자
        public NumericFieldValue(Number value, int order) {
            if (value instanceof Integer || value instanceof Double) {
                this.value = value;
            } else {
                throw new IllegalArgumentException("Invalid numeric value type. Must be an int or double.");
            }
            this.order = order;
        }
    }


    @Getter
    @Setter
    public static class StringFieldValue {
        private String value;
        private int order;

        // StringFieldValue 생성자 - 문자열 값만 허용
        public StringFieldValue(String value, int order) {
            this.value = value;
            this.order = order;
        }
    }
}
