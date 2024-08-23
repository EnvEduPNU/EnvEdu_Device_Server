package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssignmentStepContentService {
    Mono<Void> saveStepContents(AssignmentStepContent assignmentStepContent);

    Flux<AssignmentStepContent> getAllStepContents();

    Mono<Void> deleteContent(String uuid, String timestamp);

    Mono<Void> updateStepContents(String uuid, String timestamp, AssignmentStepContent assignmentStepContent);
}
