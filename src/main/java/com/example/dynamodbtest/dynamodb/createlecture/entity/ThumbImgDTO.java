package com.example.dynamodbtest.dynamodb.createlecture.entity;

import lombok.Data;

@Data
public class ThumbImgDTO {
    private String uuid;       // DynamoDB의 파티션 키
    private String timestamp;  // DynamoDB의 정렬 키
    private String thumbImg;   // 업데이트할 이미지 URL 또는 경로
}
