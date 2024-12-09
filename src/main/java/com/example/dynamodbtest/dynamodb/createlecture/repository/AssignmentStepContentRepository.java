package com.example.dynamodbtest.dynamodb.createlecture.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Repository
public class AssignmentStepContentRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public AssignmentStepContentRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Mono<AssignmentStepContent> save(AssignmentStepContent assignmentStepContent) {
        return Mono.fromRunnable(() -> dynamoDBMapper.save(assignmentStepContent))
                .thenReturn(assignmentStepContent);
    }

    public Mono<AssignmentStepContent> findByStepName(String stepName) {
        return Mono.fromCallable(() -> dynamoDBMapper.load(AssignmentStepContent.class, stepName));
    }

    public Flux<AssignmentStepContent> getStepContentByUuid(String uuid) {
        // Partition key를 설정하여 해당 UUID로 조회
        AssignmentStepContent partitionKey = new AssignmentStepContent();
        partitionKey.setUuid(uuid);

        // DynamoDBQueryExpression을 사용하여 UUID에 해당하는 모든 항목을 조회
        DynamoDBQueryExpression<AssignmentStepContent> queryExpression = new DynamoDBQueryExpression<AssignmentStepContent>()
                .withHashKeyValues(partitionKey);

        // 여러 개의 결과를 가져오기 위해 query를 사용
        List<AssignmentStepContent> result = dynamoDBMapper.query(AssignmentStepContent.class, queryExpression);

        // 결과를 Flux로 변환하여 반환
        return Flux.fromIterable(result);
    }

    public AssignmentStepContent findByUuidAndUsername(String uuid, String username) {
        AssignmentStepContent queryObject = new AssignmentStepContent();
        queryObject.setUuid(uuid);
        queryObject.setUsername(username);

        DynamoDBQueryExpression<AssignmentStepContent> queryExpression = new DynamoDBQueryExpression<AssignmentStepContent>()
                .withHashKeyValues(queryObject)
                .withConsistentRead(false);

        List<AssignmentStepContent> results = dynamoDBMapper.query(AssignmentStepContent.class, queryExpression);

        if (results != null && !results.isEmpty()) {
            return results.get(0); // 첫 번째 결과 반환
        }
        return null; // 결과가 없을 경우 null 반환
    }


    public Mono<Void> deleteByStepName(String uuid, String timestamp) {
        return Mono.fromRunnable(() -> {
            // DynamoDBMapper를 사용하여 아이템 로드
            AssignmentStepContent assignmentStepContent = dynamoDBMapper.load(AssignmentStepContent.class, uuid, timestamp);

            log.info("uuid : " + uuid);
            log.info("timestamp : " + timestamp);
            log.info("스텝 컨텐츠 : " + assignmentStepContent);

            if (assignmentStepContent != null) {
                dynamoDBMapper.delete(assignmentStepContent);
            }
        }).then();
    }


    public Flux<AssignmentStepContent> findAll() {
        return Mono.fromCallable(() -> dynamoDBMapper.scan(AssignmentStepContent.class, new DynamoDBScanExpression()))
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Void> updateStepContent(String uuid, String timestamp, AssignmentStepContent updatedAssignmentStepContent) {
        log.info("uuid : " + uuid);
        log.info("timestamp : " + timestamp);
        log.info("스텝 컨텐츠 : " + updatedAssignmentStepContent);

        AssignmentStepContent existingAssignmentStepContent = dynamoDBMapper.load(AssignmentStepContent.class, uuid);

        if (existingAssignmentStepContent == null) {
            // 기존 데이터가 없을 경우, 바로 새 데이터를 저장
            return Mono.fromRunnable(() -> {
                dynamoDBMapper.save(updatedAssignmentStepContent);
            }).then();
        }

        // 기존의 contents 리스트
        List<AssignmentStepContent.ContentWrapper> existingContents = existingAssignmentStepContent.getContents();
        // 업데이트할 contents 리스트
        List<AssignmentStepContent.ContentWrapper> updatedContents = updatedAssignmentStepContent.getContents();

        // 기존 contents 리스트를 순회하며 업데이트 로직 수행
        for (AssignmentStepContent.ContentWrapper updatedContentWrapper : updatedContents) {
            boolean found = false;
            for (int i = 0; i < existingContents.size(); i++) {
                AssignmentStepContent.ContentWrapper existingContentWrapper = existingContents.get(i);
                if (existingContentWrapper.getStepNum() == updatedContentWrapper.getStepNum()) {
                    // stepNum이 같으면 기존 contentWrapper를 업데이트된 contentWrapper로 교체
                    existingContents.set(i, updatedContentWrapper);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // 같은 stepNum이 없으면 새로 추가
                existingContents.add(updatedContentWrapper);
            }
        }

        // 기존 객체에 수정된 contents를 반영
        existingAssignmentStepContent.setContents(existingContents);

        // DynamoDB에 업데이트된 객체 저장
        return Mono.fromRunnable(() -> {
            dynamoDBMapper.save(existingAssignmentStepContent);
        }).then();
    }



}