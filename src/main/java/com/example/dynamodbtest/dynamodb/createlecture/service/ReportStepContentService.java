package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.entity.ReportStepContent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReportStepContentService {
    Mono<Void> saveStepContents(ReportStepContent reportStepContent);

    Flux<ReportStepContent> getAllStepContents();

    Mono<Void> deleteContent(String uuid, String timestamp);

    Mono<Void> updateStepContents(String uuid, String timestamp, ReportStepContent reportStepContent);

    List<ReportStepContent> getStepContentByUuid(List<String> uuid);
}
