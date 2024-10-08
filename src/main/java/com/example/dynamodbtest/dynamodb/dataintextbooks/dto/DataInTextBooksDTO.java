package com.example.dynamodbtest.dynamodb.dataintextbooks.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataInTextBooksDTO {

    private String uuid;
    private String title;
    private String content;
    private String dataTypeLabel;
    private String gradeLabel;
    private String subjectLabel;
    private List<String> properties;
    private List<Map<String,String>> data;

}
