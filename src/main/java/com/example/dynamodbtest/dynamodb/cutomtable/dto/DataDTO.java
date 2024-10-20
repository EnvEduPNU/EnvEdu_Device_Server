package com.example.dynamodbtest.dynamodb.cutomtable.dto;

import com.example.dynamodbtest.dynamodb.cutomtable.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DataDTO {

    private String dataUUID;
    private String saveDate;
    private String memo;
    private String dataLabel;
    private String userName;

    // 각 필드는 value와 order를 포함하는 구조로 수정
    private List<Map<String, FieldValue>> numericFields;
    private List<Map<String, FieldValue>> stringFields;

    // 생성자
    public DataDTO(String dataUUID, String saveDate, String memo, String dataLabel, String userName,
                   List<Map<String, FieldValue>> numericFields, List<Map<String, FieldValue>> stringFields) {
        this.dataUUID = dataUUID;
        this.saveDate = saveDate;
        this.memo = memo;
        this.dataLabel = dataLabel;
        this.userName = userName;
        this.numericFields = numericFields;
        this.stringFields = stringFields;
    }
    

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
