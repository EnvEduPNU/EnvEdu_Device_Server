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



    @DeleteMapping("/delete/{uuid}/{timestamp}")
    public Mono<Void> deleteLectureContent(@PathVariable String uuid, @PathVariable String timestamp) {
        return assignmentStepContentService.deleteContent(uuid, timestamp);
    }


}
