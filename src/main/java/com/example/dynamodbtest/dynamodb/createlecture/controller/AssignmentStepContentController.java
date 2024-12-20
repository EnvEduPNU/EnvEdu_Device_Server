package com.example.dynamodbtest.dynamodb.createlecture.controller;

import com.example.dynamodbtest.dynamodb.createlecture.entity.AssignmentStepContent;
import com.example.dynamodbtest.dynamodb.createlecture.entity.StepContent;
import com.example.dynamodbtest.dynamodb.createlecture.service.AssignmentStepContentServiceImpl;
import com.example.dynamodbtest.dynamodb.createlecture.service.StepContentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
// 학생의 과제를 각 스텝별로 저장하는 메서드로 개발되었고 사용되었으나
// 기획 수정 이후로 사용되지 않음. 현재 entity table 이름으로 aws dynamodb에 테이블 존재함.
@RequestMapping("/api/assignment")
public class AssignmentStepContentController {

    private final AssignmentStepContentServiceImpl assignmentStepContentService;

    @PostMapping("/save")
    public Mono<Void> saveStepContent(@RequestBody List<AssignmentStepContent> assignmentStepContent) {

        AssignmentStepContent formattedAssignmentStepContent = assignmentStepContent.get(0);

        log.info("save 들어온 것들 확인 : " + formattedAssignmentStepContent.getStepName());
        return assignmentStepContentService.saveStepContents(formattedAssignmentStepContent);
    }

    @PutMapping("/update")
    public Mono<Void> updateStepContent(@RequestBody List<AssignmentStepContent> assignmentStepContent) {
        AssignmentStepContent formattedAssignmentStepContent = assignmentStepContent.get(0);

        return assignmentStepContentService.updateStepContents(formattedAssignmentStepContent.getUuid(), formattedAssignmentStepContent.getTimestamp(), formattedAssignmentStepContent);
    }


    @GetMapping("/get")
    public Flux<AssignmentStepContent> getStepContent() {
        return assignmentStepContentService.getAllStepContents();
    }

    @GetMapping("/getstep")
    public Flux<AssignmentStepContent> getStepContentByUuid(@RequestParam String uuid) {
        log.info("Lecture Content Called for UUID: " + uuid);
        return assignmentStepContentService.getStepContentByUuid(uuid);
    }

    @GetMapping("/get-step-one")
    public AssignmentStepContent getStepContentByUuidAndUsername(@RequestParam String uuid, String username) {
        log.info("Lecture Content Called for UUID: " + uuid);
        log.info("Lecture Content Called for username: " + username);

        AssignmentStepContent assignmentStepContent = assignmentStepContentService.getStepContentByUuidAndUsername(uuid,username);

        log.info("Lecture Check : " + assignmentStepContent);

        return assignmentStepContent;
    }



    @DeleteMapping("/delete/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return assignmentStepContentService.deleteContent(uuid, timestamp);
    }


}
