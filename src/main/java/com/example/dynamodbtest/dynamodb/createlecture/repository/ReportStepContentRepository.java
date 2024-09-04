package com.example.dynamodbtest.dynamodb.createlecture.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dynamodb.createlecture.entity.ReportStepContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Repository
public class ReportStepContentRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public ReportStepContentRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Mono<ReportStepContent> save(ReportStepContent reportStepContent) {
        return Mono.fromRunnable(() -> dynamoDBMapper.save(reportStepContent))
                .thenReturn(reportStepContent);
    }

    public Mono<ReportStepContent> findByStepName(String stepName) {
        return Mono.fromCallable(() -> dynamoDBMapper.load(ReportStepContent.class, stepName));
    }

    public List<ReportStepContent> getStepContentByUuid(List<String> uuids) {
        // Map으로 여러 항목을 한 번에 가져오기 위한 준비
        Map<String, List<Object>> batchLoadMap = new HashMap<>();

        // 각 UUID에 해당하는 쿼리 결과를 저장할 리스트 생성
        List<ReportStepContent> resultList = new ArrayList<>();

        // UUID 리스트를 순회하며 각 항목을 DynamoDB에서 조회
        for (String uuid : uuids) {
            // UUID에 해당하는 파티션 키 값을 설정하여 쿼리
            ReportStepContent partitionKey = new ReportStepContent();
            partitionKey.setUuid(uuid);

            // DynamoDBQueryExpression을 사용하여 해당 UUID에 맞는 데이터 조회
            DynamoDBQueryExpression<ReportStepContent> queryExpression = new DynamoDBQueryExpression<ReportStepContent>()
                    .withHashKeyValues(partitionKey);

            // 조회된 결과를 리스트에 추가
            List<ReportStepContent> queryResult = dynamoDBMapper.query(ReportStepContent.class, queryExpression);
            resultList.addAll(queryResult);
        }

        if (resultList.isEmpty()) {
            log.warn("No results found for the provided UUIDs.");
            return Collections.emptyList(); // 결과가 없을 경우 빈 리스트 반환
        }

        // 로그 출력
        log.info("레포지토리에서 UUID에 해당하는 ReportStepContent 항목 수: " + resultList.size());

        return resultList;
    }






    public Mono<Void> deleteByStepName(String uuid, String timestamp) {
        return Mono.fromRunnable(() -> {
            // DynamoDBMapper를 사용하여 아이템 로드
            ReportStepContent reportStepContent = dynamoDBMapper.load(ReportStepContent.class, uuid, timestamp);

            log.info("uuid : " + uuid);
            log.info("timestamp : " + timestamp);
            log.info("스텝 컨텐츠 : " + reportStepContent);

            if (reportStepContent != null) {
                dynamoDBMapper.delete(reportStepContent);
            }
        }).then();
    }


    public Flux<ReportStepContent> findAll() {
        return Mono.fromCallable(() -> dynamoDBMapper.scan(ReportStepContent.class, new DynamoDBScanExpression()))
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Void> updateStepContent(String uuid, String timestamp, ReportStepContent updatedReportStepContent) {
        log.info("uuid : " + uuid);
        log.info("timestamp : " + timestamp);
        log.info("스텝 컨텐츠 : " + updatedReportStepContent);

        ReportStepContent existingReportStepContent = dynamoDBMapper.load(ReportStepContent.class, uuid);

        if (existingReportStepContent == null) {
            // 기존 데이터가 없을 경우, 바로 새 데이터를 저장
            return Mono.fromRunnable(() -> {
                dynamoDBMapper.save(updatedReportStepContent);
            }).then();
        }

        // 기존의 contents 리스트
        List<ReportStepContent.ContentWrapper> existingContents = existingReportStepContent.getContents();
        // 업데이트할 contents 리스트
        List<ReportStepContent.ContentWrapper> updatedContents = existingReportStepContent.getContents();

        // 기존 contents 리스트를 순회하며 업데이트 로직 수행
        for (ReportStepContent.ContentWrapper updatedContentWrapper : updatedContents) {
            boolean found = false;
            for (int i = 0; i < existingContents.size(); i++) {
                ReportStepContent.ContentWrapper existingContentWrapper = existingContents.get(i);
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
        existingReportStepContent.setContents(existingContents);

        // DynamoDB에 업데이트된 객체 저장
        return Mono.fromRunnable(() -> {
            dynamoDBMapper.save(existingReportStepContent);
        }).then();
    }



}