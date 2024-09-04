package com.example.dynamodbtest.dynamodb.createlecture.service;

import com.example.dynamodbtest.dynamodb.createlecture.entity.ReportStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.repository.ReportStepContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportStepContentServiceImpl implements ReportStepContentService {

    private final ReportStepContentRepository reportStepContentRepository;

    @Override
    public Mono<Void> saveStepContents(ReportStepContent reportStepContent) {
        return reportStepContentRepository.save(reportStepContent).then();
    }

    @Override
    public Flux<ReportStepContent> getAllStepContents() {
        return reportStepContentRepository.findAll();
    }

    @Override
    public Mono<Void> deleteContent(String uuid, String timestamp) {
        return reportStepContentRepository.deleteByStepName(uuid,timestamp);

    }

    @Override
    public Mono<Void> updateStepContents(String uuid, String timestamp, ReportStepContent reportStepContent) {
        return reportStepContentRepository.updateStepContent(uuid,timestamp,reportStepContent);
    }

    @Override
    public List<ReportStepContent> getStepContentByUuid(List<String> uuid) {
        return  reportStepContentRepository.getStepContentByUuid(uuid);
    }
}
