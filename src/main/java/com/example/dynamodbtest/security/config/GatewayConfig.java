package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


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
                .route("example_route", r -> r.path("/login/**", "/seed/**", "/mydata/**", "/datafolder/**", "/api/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            // 현재 요청을 가져옴
                            ServerHttpRequest request = exchange.getRequest();

                            // 요청 헤더를 복사하여 새로운 요청 빌더를 생성
                            ServerHttpRequest.Builder requestBuilder = request.mutate();

                            // 모든 헤더를 그대로 복사
                            request.getHeaders().forEach((key, values) -> {
                                values.forEach(value -> requestBuilder.header(key, value));
                            });

                            // 새로운 요청 생성 (본문은 변경하지 않음)
                            ServerHttpRequest mutatedRequest = requestBuilder.build();

                            // 새로운 요청을 사용하여 다음 필터 또는 최종 목적지로 요청을 전달
                            return chain.filter(exchange.mutate().request(mutatedRequest).build());
                        }))
                        .uri("https://server.greenseed.or.kr"))


                .route("example_route", r -> r.path("/ws/**")
                        .uri("https://server.greenseed.or.kr"))
                .route("example_route", r -> r.path("/screen-share/**")
                        .uri("https://server.greenseed.or.kr"))
                .build();
    }






}
