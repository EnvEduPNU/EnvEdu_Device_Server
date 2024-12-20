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
// 교사의 eclass 생성 쪽 클래스
@RequestMapping("/api/steps")
public class StepContentController {

    private final StepContentServiceImpl stepContentService;

    // eclass 생성 메서드
    @PostMapping("/saveLectureContent")
    public Mono<Void> saveStepContent(@RequestBody StepContent stepContents) {

        return stepContentService.saveStepContents(stepContents);
    }

    // eclass 수정 메서드
    @PatchMapping("/updateLectureContent")
    public Mono<Void> updateStepContent(@RequestBody StepContent stepContents) {

        return stepContentService.updateStepContents(stepContents.getUuid(), stepContents.getTimestamp(), stepContents);
    }

    // eclass 썸네일 수정 메서드
    @PatchMapping("/updateThumbImg")
    public Mono<Void> updateThumbImg(@RequestBody ThumbImgDTO thumbImgDTO) {
        return stepContentService.updateThumbImg(thumbImgDTO.getUuid(), thumbImgDTO.getTimestamp(), thumbImgDTO.getThumbImg());
    }

    // 모든 수업 자료 조회 메서드
    @GetMapping("/getLectureContent")
    public Flux<StepContent> getStepContent() {
        log.info("Lecure Content Called? : ");
        return stepContentService.getAllStepContents();
    }

    // 수업자료 uuid로 하나의 수업 자료만 조회 해오는 메서드
    @GetMapping("/getLectureContentOne")
    public StepContent getStepContentOne(@RequestParam String uuid) {
        return stepContentService.getStepContent(uuid);
    }

    // 수업 자료 삭제 메서드
    @DeleteMapping("/deleteLectureContent/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return stepContentService.deleteContent(uuid, timestamp);
    }


}
