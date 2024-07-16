package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import com.example.dynamodbtest.dynamodb.createlecture.repository.StepContentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StepContentService {
    Mono<Void> saveStepContents(StepContent stepContents);

    Flux<StepContent> getAllStepContents();

    Mono<Void> deleteContent(String uuid, String timestamp);

    Mono<Void> updateStepContents(String uuid, String timestamp, StepContent stepContents);
}
