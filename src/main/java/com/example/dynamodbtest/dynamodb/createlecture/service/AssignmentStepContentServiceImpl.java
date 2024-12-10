package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.repository.AssignmentStepContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AssignmentStepContentServiceImpl implements AssignmentStepContentService {

    private final AssignmentStepContentRepository assignmentStepContentRepository;

    @Override
    public Mono<Void> saveStepContents(AssignmentStepContent assignmentStepContent) {
        return assignmentStepContentRepository.save(assignmentStepContent).then();
    }

    @Override
    public Flux<AssignmentStepContent> getAllStepContents() {
        return assignmentStepContentRepository.findAll();
    }

    @Override
    public Mono<Void> deleteContent(String uuid, String timestamp) {
        return assignmentStepContentRepository.deleteByStepName(uuid,timestamp);

    }

    @Override
    public Mono<Void> updateStepContents(String uuid, String timestamp, AssignmentStepContent assignmentStepContent) {
        return assignmentStepContentRepository.updateStepContent(uuid,timestamp,assignmentStepContent);
    }


    public Flux<AssignmentStepContent> getStepContentByUuid(String uuid) {
        return  assignmentStepContentRepository.getStepContentByUuid(uuid);
    }

    public AssignmentStepContent getStepContentByUuidAndUsername(String uuid, String username) {
        return  assignmentStepContentRepository.findByUuidAndUsername(uuid,username);
    }
}
