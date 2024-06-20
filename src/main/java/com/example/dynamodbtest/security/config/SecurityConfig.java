package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * Spring Cloud Security 설정
 * ver 6.1
 * @author 부산대 과학교육연구소 연구보조원 김선규
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .headers(headerSpec -> headerSpec.frameOptions(frameOptionsSpec -> frameOptionsSpec.disable()));

        http
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http
                .authorizeExchange((exchanges) ->
                                exchanges
//                                .pathMatchers("/**").authenticated()
//                                .pathMatchers(HttpMethod.POST, "/users").hasAuthority("USER_POST")
//                                .matchers(customMatcher()).hasRole("CUSTOM")
                                        .pathMatchers("/**").permitAll()
                                        .anyExchange().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://new.greenseed.or.kr"));  // 허용할 출처
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));  // 허용할 HTTP 메소드
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));  // 허용할 헤더
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));  // 브라우저에서 접근 가능한 헤더
        configuration.setAllowCredentials(true);  // 인증 정보(쿠키, HTTP 인증, SSL 인증 등) 포함 허용
        configuration.setMaxAge(3600L);  // 사전 요청 결과 최대 나이(초)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/login/**", configuration);
        source.registerCorsConfiguration("/seed/**", configuration);
        source.registerCorsConfiguration("/mydata/**", configuration);
        source.registerCorsConfiguration("/datafolder/**", configuration);
        source.registerCorsConfiguration("/ocean-quality/**", configuration);
        source.registerCorsConfiguration("/air-quality/**", configuration);
        source.registerCorsConfiguration("/register-session/**", configuration);
        source.registerCorsConfiguration("/get-session-ids/**", configuration);
        source.registerCorsConfiguration("/ws/**", null);
        source.registerCorsConfiguration("/screen-share/**", null);

        return source;
    }

}