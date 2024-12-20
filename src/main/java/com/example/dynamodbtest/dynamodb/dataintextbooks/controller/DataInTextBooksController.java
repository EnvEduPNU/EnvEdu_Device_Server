package com.example.dynamodbtest.dynamodb.dataintextbooks.controller;

import com.example.dynamodbtest.dynamodb.dataintextbooks.dto.DataInTextBooksDTO;
import com.example.dynamodbtest.dynamodb.dataintextbooks.service.DataInTextBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// 정적인 데이터, DataChart 메뉴의 MyData 페이지에 TextBook에 있는 Data들도 추가할 수 있어요에 있는 테이블 데이터들 조회/저장/삭제 메서드
@RequestMapping("/api/data-in-textbooks")
public class DataInTextBooksController {

    private final DataInTextBooksService service;

    @Autowired
    public DataInTextBooksController(DataInTextBooksService service) {
        this.service = service;
    }

    @GetMapping("/{uuid}")
    public DataInTextBooksDTO getById(@PathVariable String uuid) {
        return service.getById(uuid);
    }

    @GetMapping("/getAllRecords")
    public List<DataInTextBooksDTO> getAllRecords() {
        return service.getAllRecords();
    }

    @PostMapping
    public void createRecord(@RequestBody DataInTextBooksDTO dto) {
        service.save(dto);
    }

    @DeleteMapping("/{uuid}")
    public void deleteRecord(@PathVariable String uuid) {
        service.delete(uuid);
    }
}
