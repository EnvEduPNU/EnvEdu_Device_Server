package com.example.dynamodbtest.dynamodb.createlecture.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class StepContentRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public StepContentRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Mono<StepContent> save(StepContent stepContent) {
        return Mono.fromRunnable(() -> dynamoDBMapper.save(stepContent))
                .thenReturn(stepContent);
    }

    public Mono<StepContent> findByStepName(String stepName) {
        return Mono.fromCallable(() -> dynamoDBMapper.load(StepContent.class, stepName));
    }

    public Mono<Void> deleteByStepName(String stepName) {
        return Mono.fromRunnable(() -> {
            StepContent stepContent = dynamoDBMapper.load(StepContent.class, stepName);
            if (stepContent != null) {
                dynamoDBMapper.delete(stepContent);
            }
        }).then();
    }

    public Flux<StepContent> findAll() {
        return Mono.fromCallable(() -> dynamoDBMapper.scan(StepContent.class, new DynamoDBScanExpression()))
                .flatMapMany(Flux::fromIterable);
    }
}