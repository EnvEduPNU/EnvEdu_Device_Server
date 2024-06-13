package com.example.dynamodbtest.security.authentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Client 에서 들어 오는 사용자의 이름과 password로 인증하는 필터 메서드
 */
@Component
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
