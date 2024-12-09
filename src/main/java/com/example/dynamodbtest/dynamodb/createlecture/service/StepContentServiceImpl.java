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
    public StepContent getStepContent(String uuid) {
        return stepContentRepository.findOne(uuid);
    }

    @Override
    public Mono<Void> deleteContent(String uuid, String timestamp) {
        return stepContentRepository.deleteByStepName(uuid,timestamp);

    }

    @Override
    public Mono<Void> updateStepContents(String uuid, String timestamp, StepContent stepContents) {
        return stepContentRepository.updateStepContent(uuid,timestamp,stepContents);
    }

    @Override
    public Mono<Void> updateThumbImg(String uuid, String timestamp, String thumbImg) {
        return stepContentRepository.updateThumbImg(uuid,timestamp,thumbImg);
    }




}
