package com.example.dynamodbtest.security.filter;
import com.example.dynamodbtest.security.authentication.CustomReactiveAuthenticationManager;
import com.example.dynamodbtest.security.jwt.JwtTokenUtil;
import com.example.dynamodbtest.security.principal.CustomUserDetails;
import com.example.dynamodbtest.user.dto.request.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
public class AuthenticationFilter extends AuthenticationWebFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomReactiveAuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationFilter(CustomReactiveAuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super( authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Filter entered");


        // WebSocket 경로 확인
        if (exchange.getRequest().getURI().getPath().startsWith("/ws")) {
            ServerHttpRequest request = exchange.getRequest();
            String tokenInParams = request.getHeaders().getFirst("Authorization");

            log.info("토큰: " + tokenInParams);

            if (tokenInParams != null && tokenInParams.startsWith("Bearer ")) {
                String token = tokenInParams.substring(7);
                log.info("토큰 체크 : {}", token);

                try {
                    Claims claims = jwtTokenUtil.validateToken(tokenInParams);
                    String userName = claims.getSubject();
                    exchange.getAttributes().put("userName", userName);
                    log.info("WebSocket 경로에서 토큰으로 인증 됨");
                    return onAuthenticationSuccessWebsocket(null, exchange, chain);
                } catch (Exception e) {
                    log.info("Invalid JWT token in WebSocket connection");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
        }

        // WebSocket이 아닌 경우 일반 HTTP 요청 처리
        Mono<String> authHeader = Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"));

        // JWT 토큰 있으면 통과
        return authHeader
                .flatMap(header -> {
                    if (header.startsWith("Bearer ")) {
                        String token = header.substring(7);

                        try {
                            Claims claims = jwtTokenUtil.validateToken(token);
                            String userName = claims.getSubject();
                            exchange.getAttributes().put("userName", userName);
                            log.info("토큰으로 인증 됨!");

                            return onAuthenticationSuccess(null, exchange, chain);
                        } catch (Exception e) {
                            log.info("Invalid JWT token");
                            return Mono.empty();
                        }
                    } else {
                        log.info("Authorization header does not start with Bearer");
                        return Mono.empty();
                    }
                })
                // Authorization 헤더가 없을 경우 사용자 이름과 비밀번호로 인증 시도
                .switchIfEmpty(authenticateByUsernameAndPassword(exchange, chain))
                .onErrorResume(e -> {
                    log.error("Authentication failed", e);
                    return Mono.empty();
                });

    }

    // 사용자 이름과 비밀번호 인증 시도
    private Mono<Void> authenticateByUsernameAndPassword(ServerWebExchange exchange, WebFilterChain chain) {

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    try {
                        String body = dataBuffer.toString(StandardCharsets.UTF_8);
                        LoginDTO loginDTO = objectMapper.readValue(body, LoginDTO.class);

                        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
                        return authenticationManager.authenticate(authenticationToken)
                                .flatMap(authentication -> onAuthenticationSuccess(authentication, exchange, chain))
                                .onErrorResume(AuthenticationException.class, e -> onAuthenticationFailure(e, exchange, chain));
                    } catch (Exception e) {
                        log.error("Error during username/password authentication", e);
                        return Mono.error(e);
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                });
    }


    // 인증 완료 시 진행되는 메서드
    private Mono<Void> onAuthenticationSuccess(Authentication authentication, ServerWebExchange exchange, WebFilterChain chain) {

        // jwt 토큰 없고 아이디 비번으로만 로그인 인증 했을때 토큰 만들어서 보내줌
        if (authentication != null) {
            // JWT 토큰 생성
            String userName = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            log.info("유저 권한: {}", role);
            log.info("유저이름 : {}", userName);


            String token = jwtTokenUtil.generateToken(userName,role);
            // JWT 토큰을 Authorization 헤더에 추가
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("Authorization", "Bearer " + token)
                    .header("userName", userName)
                    .build();
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            log.info("Authentication successful");

            return chain.filter(mutatedExchange);
        }
        // jwt 토큰 있어서 그 토큰 만으로 인증
        else {
            log.info("체크");

            Mono<String> authHeader = Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"));

            return authHeader.flatMap(token -> {
                log.info("토큰 잘 들어갔나: {}", token);

                String userName;
                try {
                    userName = JwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
                    log.info("추출된 유저 이름: {}", userName);

                } catch (Exception e) {
                    log.error("토큰 파싱 중 오류 발생: ", e);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("Authorization", token)
                        .header("userName", userName)
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                log.info("Authentication successful");

                return chain.filter(mutatedExchange);
            }).onErrorResume(error -> {
                log.error("토큰 로드 중 오류 발생: ", error);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            });

        }

    }

    private Mono<Void> onAuthenticationSuccessWebsocket(Authentication authentication, ServerWebExchange exchange, WebFilterChain chain) {

        String tokenInParams = exchange.getRequest().getQueryParams().getFirst("token");

        log.info("토큰 잘 들어갔나: {}", tokenInParams);

        String userName;
        try {
            userName = JwtTokenUtil.getUsernameFromToken(tokenInParams);
            log.info("추출된 유저 이름: {}", userName);

        } catch (Exception e) {
            log.error("토큰 파싱 중 오류 발생: ", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("Authorization", tokenInParams)
                .header("userName", userName)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
        log.info("Authentication successful");

        return chain.filter(mutatedExchange);
    }

    // 인증 실패 시 메서드
    private Mono<Void> onAuthenticationFailure(AuthenticationException e, ServerWebExchange exchange, WebFilterChain chain) {
        log.error("Authentication failed: {}", e.getMessage());
        return Mono.error(e);
    }

}
