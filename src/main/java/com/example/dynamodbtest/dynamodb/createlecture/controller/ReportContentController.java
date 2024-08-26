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

    @GetMapping("/getstep")
    public Flux<ReportStepContent> getStepContentByUuid(@RequestParam String uuid) {
        log.info("Lecture Content Called for UUID: " + uuid);
        return reportStepContentService.getStepContentByUuid(uuid);
    }



    @DeleteMapping("/delete/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return reportStepContentService.deleteContent(uuid, timestamp);
    }


}
