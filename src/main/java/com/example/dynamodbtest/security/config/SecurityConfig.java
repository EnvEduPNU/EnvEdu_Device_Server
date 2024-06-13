package com.example.dynamodbtest.security.config;

import com.example.dynamodbtest.security.authentication.CustomReactiveAuthenticationManager;
import com.example.dynamodbtest.security.context.CustomSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Cloud Security 설정
 * ver 6.1
 * @author 부산대 과학교육연구소 연구보조원 김선규
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, CustomSecurityContextRepository securityContextRepository) {

        http    .csrf(ServerHttpSecurity.CsrfSpec::disable);

        // 도메인 간 접근 시 보안 접근 Path 관리
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("https://example.com"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST"));
                    return config;
                    }));

        // Client 에서 접근시 기본 인증 과정
        http
                .httpBasic((httpBasic) ->
                httpBasic
                        // Credential 을 가지고 인증
                        .authenticationManager(new CustomReactiveAuthenticationManager())
                        // Token 을 가지고 인증
                        .securityContextRepository(securityContextRepository)
        );

        // 인증 완료 한 후 인가 과정
        http

                .authorizeExchange((exchanges) ->
                        exchanges
                                // any URL that starts with /admin/ requires the role "ROLE_ADMIN"
                                .pathMatchers("/admin/**").hasRole("ADMIN")
                                // a POST to /users requires the role "USER_POST"
                                .pathMatchers(HttpMethod.POST, "/users").hasAuthority("USER_POST")


// ---> 아래의 방식으로도 인증/인가를 사용할 수 있다. (spring-security-docs 6.3.0 API)
                                // a request to /users/{username} requires the current authentication's username
                                // to be equal to the {username}
//                                .pathMatchers("/users/{username}").access((authentication, context) ->
//                                        authentication
//                                                .map(Authentication::getName)
//                                                .map((username) -> username.equals(context.getVariables().get("username")))
//                                                .map(AuthorizationDecision::new)
//                                )
                                // allows providing a custom matching strategy that requires the role "ROLE_CUSTOM"
                                .matchers(customMatcher()).hasRole("CUSTOM")
                                // any other request requires the user to be authenticated
                                .anyExchange().authenticated()
                );

        return http.build();
    }


    // 사용자 정의 매처
    public ServerWebExchangeMatcher customMatcher() {
        return exchange -> exchange.getRequest().getPath().value().startsWith("/custom")
                ? ServerWebExchangeMatcher.MatchResult.match()
                : ServerWebExchangeMatcher.MatchResult.notMatch();
    }
}
