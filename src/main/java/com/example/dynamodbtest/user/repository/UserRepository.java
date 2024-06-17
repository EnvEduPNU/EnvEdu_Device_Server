package com.example.dynamodbtest.user.repository;

import com.example.dynamodbtest.user.dto.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM User WHERE username = :name")
    Mono<User> findByUsername(String name);
}

