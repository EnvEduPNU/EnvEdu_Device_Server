package com.example.dynamodbtest.dynamodb.createlecture.controller;

import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.entity.ReportStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.service.AssignmentStepContentServiceImpl;
import com.example.dynamodbtest.dynamodb.createlecture.service.ReportStepContentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
// 학생의 보고서 저장 클래스, 각 path의 역할에 맞게 메서드 수행중 (ex save -> 보고서 저장)
@RequestMapping("/api/report")
public class ReportContentController {

    private final ReportStepContentServiceImpl reportStepContentService;

    @PostMapping("/save")
    public Mono<Void> saveStepContent(@RequestBody List<ReportStepContent> reportStepContents) {

        ReportStepContent formattedReportStepContent = reportStepContents.get(0);

        log.info("save 들어온 것들 확인 : " + formattedReportStepContent.getStepName());
        return reportStepContentService.saveStepContents(formattedReportStepContent);
    }

    @PutMapping("/update")
    public Mono<Void> updateStepContent(@RequestBody List<ReportStepContent> assignmentStepContent) {
        ReportStepContent formattedReportStepContent = assignmentStepContent.get(0);

        return reportStepContentService.updateStepContents(formattedReportStepContent.getUuid(), formattedReportStepContent.getTimestamp(), formattedReportStepContent);
    }


    @GetMapping("/get")
    public Flux<ReportStepContent> getStepContent() {
        return reportStepContentService.getAllStepContents();
    }

    @PostMapping("/getstep")
    public List<ReportStepContent> getStepContentByUuid(@RequestBody List<String> uuids) {
        log.info("Lecture Content Called for UUID: " + uuids.toString());

        List<ReportStepContent> reportStepContentFlux = reportStepContentService.getStepContentByUuid(uuids);

        return reportStepContentFlux;
    }




    @DeleteMapping("/delete/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return reportStepContentService.deleteContent(uuid, timestamp);
    }


}
