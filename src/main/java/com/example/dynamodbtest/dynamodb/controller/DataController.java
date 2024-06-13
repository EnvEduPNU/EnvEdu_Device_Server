package com.example.dynamodbtest.dynamodb.controller;


import com.example.dynamodbtest.dynamodb.dto.DynamicData;
import com.example.dynamodbtest.dynamodb.repository.DynamicDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dynamodb")
@RequiredArgsConstructor
public class DataController {

    private final DynamicDataRepository dynamicDataRepository;

    @PostMapping("/save")
    public List<DynamicData> createOrUpdateData(@RequestBody List<DynamicData> dataList) {
        return (List<DynamicData>) dynamicDataRepository.saveAll(dataList);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testServer() {
        // 'success' 메시지를 담은 JSON 객체를 반환
        return ResponseEntity.ok().body("{\"message\":\"success\"}");
    }


}

