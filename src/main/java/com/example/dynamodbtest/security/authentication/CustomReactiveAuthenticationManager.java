package com.example.dynamodbtest.security.authentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        // 여기서는 예시로, 유저네임과 패스워드가 "admin"일 때만 인증을 통과시키는 간단한 로직을 구현합니다.
        if ("admin".equals(username) && "admin".equals(password)) {
            User user = new User(username, password, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
            return Mono.just(new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities()));
        } else {
            return Mono.error(new RuntimeException("Authentication failed"));
        }
    }
}
