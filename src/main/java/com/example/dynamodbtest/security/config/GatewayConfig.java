package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


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
                .route("example_route", r -> r.path("/login/**","/seed/**","/mydata/**","/datafolder/**","/api/**")
                        .filters(f -> f.modifyRequestBody(String.class, String.class, (exchange, s) -> {
                                            ServerHttpRequest request = exchange.getRequest();
                                            ServerHttpRequest.Builder requestBuilder = request.mutate();

                                            log.info("문제 생길만한 요청헤더!: " + request.getHeaders());

                                            // 모든 헤더를 그대로 복사
                                            request.getHeaders().forEach((key, values) -> {
                                                values.forEach(value -> requestBuilder.header(key, value));
                                            });

                                            ServerHttpRequest mutatedRequest = requestBuilder.build();
                                            return Mono.just(mutatedRequest.getBody().toString());
                                        })
                                .modifyRequestBody(String.class, String.class, (exchange, s) -> {
                                    return exchange.getRequest()
                                            .getBody()
                                            .map(dataBuffer -> {
                                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                                dataBuffer.read(bytes);
                                                DataBufferUtils.release(dataBuffer);
                                                return new String(bytes, StandardCharsets.UTF_8);
                                            })
                                            .reduce("", (prev, str) -> prev + str); // 여러 DataBuffer를 하나의 문자열로 합칩니다.
                                })



                        )
                        .uri("https://server.greenseed.or.kr"))

                .route("example_route", r -> r.path("/ws/**")
                        .uri("https://server.greenseed.or.kr"))
                .route("example_route", r -> r.path("/screen-share/**")
                        .uri("https://server.greenseed.or.kr"))
                .build();
    }






}
