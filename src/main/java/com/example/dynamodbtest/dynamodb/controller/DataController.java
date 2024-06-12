package com.example.dynamodbtest.dynamodb.controller;


import com.example.dynamodbtest.dynamodb.dto.DynamicData;
import com.example.dynamodbtest.dynamodb.repository.DynamicDataRepository;
import lombok.RequiredArgsConstructor;
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
    }}

