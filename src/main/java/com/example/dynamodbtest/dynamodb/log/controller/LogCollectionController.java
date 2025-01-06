package com.example.dynamodbtest.dynamodb.log.controller;

import com.example.dynamodbtest.dynamodb.log.entity.LogCollection;
import com.example.dynamodbtest.dynamodb.log.service.LogCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logs")
public class LogCollectionController {

    private final LogCollectionService service;

    public LogCollectionController(LogCollectionService service) {
        this.service = service;
    }

    // Create or Update LogCollection
    @PostMapping("/save")
    public ResponseEntity<LogCollection> saveLog(@RequestBody LogCollection logCollection) {
        service.saveLogCollection(logCollection);
        return ResponseEntity.ok(logCollection);
    }

    // Get all LogCollections
    @GetMapping("/list-all")
    public ResponseEntity<List<LogCollection>> getAllLogs() {
        List<LogCollection> logs = service.getAllLogCollections();
        return ResponseEntity.ok(logs);
    }

    // Get LogCollection by logUuid
    @GetMapping("/get/{logUuid}")
    public ResponseEntity<LogCollection> getLogByLogUuid(@PathVariable String logUuid) {
        Optional<LogCollection> log = service.getLogCollectionById(logUuid);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get LogCollections by eclassUuid and username
    @GetMapping("/get/{eclassUuid}/users/{username}")
    public ResponseEntity<List<LogCollection>> getLogsByEclassUuidAndUsername(
            @PathVariable String eclassUuid,
            @PathVariable String username) {
        List<LogCollection> logs = service.getLogCollectionsByEclassUuidAndUsername(eclassUuid, username);
        return ResponseEntity.ok(logs);
    }

    // Get LogCollections by username
    @GetMapping("/get/log/{username}")
    public ResponseEntity<List<LogCollection>> getLogsByUsername(
            @PathVariable String username) {
        List<LogCollection> logs = service.getLogCollectionByUsername(username);
        return ResponseEntity.ok(logs);
    }

    // Delete LogCollection by logUuid
    @DeleteMapping("/delete/{logUuid}")
    public ResponseEntity<Void> deleteLogByLogUuid(@PathVariable String logUuid) {
        service.deleteLogCollectionById(logUuid);
        return ResponseEntity.noContent().build();
    }
}
