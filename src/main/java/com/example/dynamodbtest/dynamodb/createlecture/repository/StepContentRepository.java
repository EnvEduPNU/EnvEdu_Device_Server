package com.example.dynamodbtest.dynamodb.createlecture.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
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

    public Mono<Void> deleteByStepName(String uuid, String timestamp) {
        return Mono.fromRunnable(() -> {
            // DynamoDBMapper를 사용하여 아이템 로드
            StepContent stepContent = dynamoDBMapper.load(StepContent.class, uuid, timestamp);

            log.info("uuid : " + uuid);
            log.info("timestamp : " + timestamp);
            log.info("스텝 컨텐츠 : " + stepContent);

            if (stepContent != null) {
                dynamoDBMapper.delete(stepContent);
            }
        }).then();
    }


    public Flux<StepContent> findAll() {
        return Mono.fromCallable(() -> dynamoDBMapper.scan(StepContent.class, new DynamoDBScanExpression()))
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Void> updateStepContent(String uuid, String timestamp, StepContent updatedContent) {

//        if (updatedContent.getContents() != null) {
//            for (StepContent.ContentWrapper wrapper : updatedContent.getContents()) {
//                if (wrapper.getContents() != null) {
//                    for (StepContent.Content content : wrapper.getContents()) {
//                        log.info("컨텐츠 확인: " + content.getContent());
//                    }
//                }
//            }
//        }

        return Mono.fromRunnable(() -> {
            // DynamoDBMapper를 사용하여 아이템 로드
            StepContent stepContent = dynamoDBMapper.load(StepContent.class, uuid, timestamp);

            log.info("uuid : " + uuid);
            log.info("timestamp : " + timestamp);
            log.info("스텝 컨텐츠 : " + stepContent);

            if (stepContent != null) {
                dynamoDBMapper.save(updatedContent);
            }
        }).then();
    }

}