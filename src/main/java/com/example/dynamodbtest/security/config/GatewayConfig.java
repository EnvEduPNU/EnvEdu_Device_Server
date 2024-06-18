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
                .route("example_route", r -> r.path("/**")
                        .filters(f -> f.modifyRequestBody(String.class, String.class, (exchange, s) -> {
                                    ServerHttpRequest request = exchange.getRequest();
                                    ServerHttpRequest.Builder requestBuilder = request.mutate();

                                    log.info("문제 생길만한 요청헤더: " + request.getHeaders());

                                    // 모든 헤더를 그대로 복사
                                    request.getHeaders().forEach((key, values) -> {
                                        values.forEach(value -> requestBuilder.header(key, value));
                                    });

                                    // Content-Type 헤더가 있는 경우 복사
                                    if (request.getHeaders().getContentType() != null) {
                                        requestBuilder.header("Content-Type", request.getHeaders().getContentType().toString());
                                    }

                                    ServerHttpRequest mutatedRequest = requestBuilder.build();
                                    return Mono.just(mutatedRequest.getBody().toString());
                                })
                                .modifyResponseBody(String.class, String.class, (exchange, s) -> {
                                    return Mono.just(s);
                                }))
                        .uri("https://server.greenseed.or.kr"))
                .build();
    }





}
