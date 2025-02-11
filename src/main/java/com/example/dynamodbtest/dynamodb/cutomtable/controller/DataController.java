package com.example.dynamodbtest.dynamodb.cutomtable.controller;
import com.example.dynamodbtest.dynamodb.cutomtable.dto.DataDTO;
import com.example.dynamodbtest.dynamodb.cutomtable.entity.DataEntity;
import com.example.dynamodbtest.dynamodb.cutomtable.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
// 커스텀 데이터 저장/삭제/조회 메서드 (aws dynamodb에 저장되며 테이블 이름: CustomData)
@RequestMapping("/api/custom")
public class DataController {

    private final DataService dataService;

    @PostMapping("/save")
    public Mono<Void> saveData(@RequestBody DataDTO dataDTOs) {
        dataService.saveData(dataDTOs);
        return Mono.empty();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteData(@PathVariable String id) {
        dataService.deleteData(id);
        return Mono.empty();
    }

    @GetMapping("/list")
    public Mono<List<DataEntity>> getAllData(@RequestParam String username) {
        List<DataEntity> dataList = dataService.findAllByUserName(username);
        return Mono.justOrEmpty(dataList);
    }

    // ID로 데이터 조회 메서드 추가
    @GetMapping("/{id}")
    public Mono<DataEntity> getDataById(@PathVariable String id) {
        Optional<DataEntity> data = dataService.findById(id);
        return Mono.justOrEmpty(data);
    }



}
