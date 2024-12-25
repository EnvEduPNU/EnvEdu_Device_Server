package com.example.dynamodbtest.dynamodb.log.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dynamodbtest.dynamodb.log.entity.LogCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LogCollectionRepository {

    private final DynamoDBMapper dynamoDBMapper;

    // Save or Update LogCollection
    public void save(LogCollection entity) {
        dynamoDBMapper.save(entity);
    }

    // Delete LogCollection by logUuid
    public void deleteById(String logUuid) {
        LogCollection entity = dynamoDBMapper.load(LogCollection.class, logUuid);
        if (entity != null) {
            dynamoDBMapper.delete(entity);
        }
    }

    // Find all LogCollections
    public List<LogCollection> findAll() {
        return dynamoDBMapper.scan(LogCollection.class, new DynamoDBScanExpression());
    }

    // Find LogCollections by eclassUuid and username
    public List<LogCollection> findByEclassUuidAndUsername(String eclassUuid, String username) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("eclassUuid = :val1 AND username = :val2")
                .withExpressionAttributeValues(Map.of(
                        ":val1", new AttributeValue().withS(eclassUuid),
                        ":val2", new AttributeValue().withS(username)
                ));

        return dynamoDBMapper.scan(LogCollection.class, scanExpression);
    }

    // Find LogCollection by logUuid
    public Optional<LogCollection> findById(String logUuid) {
        LogCollection entity = dynamoDBMapper.load(LogCollection.class, logUuid);
        return Optional.ofNullable(entity);
    }
}
