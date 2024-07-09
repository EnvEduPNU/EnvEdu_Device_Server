package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import com.example.dynamodbtest.dynamodb.createlecture.repository.StepContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class StepContentServiceImpl implements StepContentService {

    private final StepContentRepository stepContentRepository;

    @Override
    public Mono<Void> saveStepContents(StepContent stepContent) {
        return stepContentRepository.save(stepContent).then();
    }

    @Override
    public Flux<StepContent> getAllStepContents() {
        return stepContentRepository.findAll();
    }

    @Override
    public Mono<Void> deleteContent(String stepName){
        return stepContentRepository.deleteByStepName(stepName);
    }

}
