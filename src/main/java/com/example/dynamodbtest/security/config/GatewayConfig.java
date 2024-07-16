package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class GatewayConfig {

    @Bean
    public WebSocketClient webSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter(WebSocketService webSocketService) {
        return new WebSocketHandlerAdapter(webSocketService);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        log.info("Gateway 설정 완료!");

        return builder.routes()
                .route("example_route", r -> r.path("/login/**","/seed/**","/mydata/**","/datafolder/**","/api/**","/ocean-quality/**", "/air-quality/**","/student/**")
                        .filters(f -> f.modifyRequestBody(String.class, String.class, (exchange, body) -> {
                            ServerHttpRequest request = exchange.getRequest();
                            log.info("문제 생길만한 요청헤더!: " + request.getHeaders());
                            log.info("문제 생길만한 요청바디!: " + body);
                            // 본문을 수정하지 않고 그대로 반환
                            return Mono.justOrEmpty(body);
                        }))
                        .uri("https://server.greenseed.or.kr"))
//                        .uri("http://localhost:8081"))


                .route("example_route", r -> r.path("/ws/**")
                        .uri("https://server.greenseed.or.kr"))
//                        .uri("http://localhost:8081"))
                .route("example_route", r -> r.path("/screen-share/**")
                        .uri("https://server.greenseed.or.kr"))
//                        .uri("http://localhost:8081"))
                .build();
    }






}
