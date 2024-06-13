package com.example.dynamodbtest.security.service;


import com.example.dynamodbtest.user.dto.User;
import com.example.dynamodbtest.user.repository.UserRepository;
import com.example.dynamodbtest.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {

        Mono<User> users = userRepository.findByUsername(username);

        Mono<UserDetails> returnObject = users
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found with username: " + username))))
                .map(user -> new CustomUserDetails(user));

        return returnObject;
    }
}
