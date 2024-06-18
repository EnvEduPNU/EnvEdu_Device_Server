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
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    ServerHttpRequest request = exchange.getRequest();
                                    ServerHttpRequest.Builder requestBuilder = request.mutate();

                                    log.info("문제 생길만한 요청헤더: " + request.getHeaders());

                                    request.getHeaders().forEach((key, values) -> {
                                        if (!key.equalsIgnoreCase("Host")) {  // Host 헤더를 제외하고 복사
                                            values.forEach(value -> requestBuilder.header(key, value));
                                        }
                                    });

                                    return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
                                }))
                        .uri("https://server.greenseed.or.kr"))
                .build();
    }

}
