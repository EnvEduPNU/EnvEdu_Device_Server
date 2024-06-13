package com.example.dynamodbtest.security.config;

import com.example.dynamodbtest.security.authentication.CustomReactiveAuthenticationManager;
import com.example.dynamodbtest.security.context.CustomSecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import com.example.dynamodbtest.security.filter.AuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Cloud Security 설정
 * ver 6.1
 * @author 부산대 과학교육연구소 연구보조원 김선규
 */
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, CustomSecurityContextRepository securityContextRepository) {

        http    .csrf(ServerHttpSecurity.CsrfSpec::disable);

        // 도메인 간 접근 시 보안 접근 Path 관리
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
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
//                                .pathMatchers("/admin/**").hasRole("ADMIN")
//                                .pathMatchers(HttpMethod.POST, "/users").hasAuthority("USER_POST")
//                                .matchers(customMatcher()).hasRole("CUSTOM")
//                                .anyExchange().authenticated()
                                .pathMatchers("/**").permitAll()
                );

        http
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);


        return http.build();
    }


    // 사용자 정의 매처
//    public ServerWebExchangeMatcher customMatcher() {
//        return exchange -> exchange.getRequest().getPath().value().startsWith("/custom")
//                ? ServerWebExchangeMatcher.MatchResult.match()
//                : ServerWebExchangeMatcher.MatchResult.notMatch();
//    }
}
