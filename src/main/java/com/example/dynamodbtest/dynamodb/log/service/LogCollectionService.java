package com.example.dynamodbtest.dynamodb.log.service;

import com.example.dynamodbtest.dynamodb.log.entity.LogCollection;
import com.example.dynamodbtest.dynamodb.log.repository.LogCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogCollectionService {

    private final LogCollectionRepository repository;

    public void saveLogCollection(LogCollection logCollection) {
        repository.save(logCollection);
    }

    public void deleteLogCollectionById(String logUuid) {
        repository.deleteById(logUuid);
    }

    public List<LogCollection> getAllLogCollections() {
        return repository.findAll();
    }

    public Optional<LogCollection> getLogCollectionById(String logUuid) {
        return repository.findById(logUuid);
    }

    public List<LogCollection> getLogCollectionsByEclassUuidAndUsername(String eclassUuid, String username) {
        return repository.findByEclassUuidAndUsername(eclassUuid, username);
    }

    public List<LogCollection> getLogCollectionByUsername(String username) {
        return repository.findByUsername(username);
    }

}
