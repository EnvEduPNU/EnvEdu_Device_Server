package com.example.dynamodbtest.dynamodb.cutomtable.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dynamodbtest.dynamodb.cutomtable.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DataRepository{

    private final DynamoDBMapper dynamoDBMapper;

    public void save(DataEntity entity) {
        dynamoDBMapper.save(entity);
    }

    public void deleteById(String dataUUID) {
        DataEntity entity = dynamoDBMapper.load(DataEntity.class, dataUUID);
        if (entity != null) {
            dynamoDBMapper.delete(entity);
        }
    }

    // 모든 데이터 조회
    public List<DataEntity> findAll() {
        return dynamoDBMapper.scan(DataEntity.class, new DynamoDBScanExpression());
    }

    public List<DataEntity> findAllByUserName(String userName) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userName = :val1")
                .withExpressionAttributeValues(Map.of(":val1", new AttributeValue().withS(userName)));

        return dynamoDBMapper.scan(DataEntity.class, scanExpression);
    }


    // ID로 데이터 찾기
    public Optional<DataEntity> findById(String dataUUID) {
        DataEntity entity = dynamoDBMapper.load(DataEntity.class, dataUUID);
        return Optional.ofNullable(entity);
    }

}
