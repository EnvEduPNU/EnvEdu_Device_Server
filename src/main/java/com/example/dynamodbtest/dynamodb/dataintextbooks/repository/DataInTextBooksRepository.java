package com.example.dynamodbtest.dynamodb.dataintextbooks.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dynamodb.dataintextbooks.model.DataInTextBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataInTextBooksRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public DataInTextBooksRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public DataInTextBooks findById(String uuid) {
        return dynamoDBMapper.load(DataInTextBooks.class, uuid);
    }

    public List<DataInTextBooks> findAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        return dynamoDBMapper.scan(DataInTextBooks.class, scanExpression);
    }

    public void save(DataInTextBooks dataInTextBooks) {
        dynamoDBMapper.save(dataInTextBooks);
    }

    public void delete(String uuid) {
        DataInTextBooks item = dynamoDBMapper.load(DataInTextBooks.class, uuid);
        if (item != null) {
            dynamoDBMapper.delete(item);
        }
    }
}
