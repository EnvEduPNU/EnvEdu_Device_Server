package com.example.dynamodbtest.dynamodb.repository;

import com.example.dynamodbtest.dynamodb.dto.DynamicData;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


@EnableScan
public interface DynamicDataRepository extends CrudRepository<DynamicData, String> {
    Optional<DynamicData> findById(String id);

}

