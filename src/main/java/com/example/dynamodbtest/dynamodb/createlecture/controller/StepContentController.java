package com.example.dynamodbtest.dynamodb.createlecture.controller;

import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import com.example.dynamodbtest.dynamodb.createlecture.service.StepContentService;
import com.example.dynamodbtest.dynamodb.createlecture.service.StepContentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class StepContentController {

    private final StepContentServiceImpl stepContentService;

    @PostMapping("/saveLectureContent")
    public Mono<Void> saveStepContent(@RequestBody StepContent stepContents) {

        log.info("확인 : " + stepContents);

        return stepContentService.saveStepContents(stepContents);
    }

    @GetMapping("/getLectureContent")
    public Flux<StepContent> getStepContent() {
        log.info("Lecure Content Called : ");
        return stepContentService.getAllStepContents();
    }

    @DeleteMapping("/deleteLectureContent/{stepName}")
    public Mono<Void> deleteLectureContentMethod(@PathVariable String stepName) {

        return stepContentService.deleteContent(stepName);
    }
}
