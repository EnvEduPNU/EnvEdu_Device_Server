package com.example.dynamodbtest.dynamodb.createlecture.controller;

import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import com.example.dynamodbtest.dynamodb.createlecture.entity.ThumbImgDTO;
import com.example.dynamodbtest.dynamodb.createlecture.service.StepContentService;
import com.example.dynamodbtest.dynamodb.createlecture.service.StepContentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class StepContentController {

    private final StepContentServiceImpl stepContentService;

    @PostMapping("/saveLectureContent")
    public Mono<Void> saveStepContent(@RequestBody StepContent stepContents) {

        return stepContentService.saveStepContents(stepContents);
    }

    @PatchMapping("/updateLectureContent")
    public Mono<Void> updateStepContent(@RequestBody StepContent stepContents) {

        return stepContentService.updateStepContents(stepContents.getUuid(), stepContents.getTimestamp(), stepContents);
    }

    @PatchMapping("/updateThumbImg")
    public Mono<Void> updateThumbImg(@RequestBody ThumbImgDTO thumbImgDTO) {
        return stepContentService.updateThumbImg(thumbImgDTO.getUuid(), thumbImgDTO.getTimestamp(), thumbImgDTO.getThumbImg());
    }



    @GetMapping("/getLectureContent")
    public Flux<StepContent> getStepContent() {
        log.info("Lecure Content Called? : ");
        return stepContentService.getAllStepContents();
    }

    @DeleteMapping("/deleteLectureContent/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return stepContentService.deleteContent(uuid, timestamp);
    }


}
