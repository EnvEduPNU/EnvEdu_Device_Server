package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.ServerWebExchange;
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
                .route("example_route", r -> r.path("/login/**","/seed/**","/mydata/**","/datafolder/**","/api/**")
                        .filters(f -> f.filter(new CustomRequestHeaderFilter().apply(new Object())))
                        .uri("https://server.greenseed.or.kr"))

                .route("example_route", r -> r.path("/ws/**")
                        .uri("https://server.greenseed.or.kr"))
                .route("example_route", r -> r.path("/screen-share/**")
                        .uri("https://server.greenseed.or.kr"))
                .build();
    }

    public static class CustomRequestHeaderFilter extends AbstractGatewayFilterFactory<Object> {
        @Override
        public GatewayFilter apply(Object config) {
            return (exchange, chain) -> {
                // 요청 헤더를 설정
                ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

                // 기존의 모든 헤더를 복사
                exchange.getRequest().getHeaders().forEach((key, values) -> {
                    values.forEach(value -> requestBuilder.header(key, value));
                });

                // 추가 헤더를 설정
                requestBuilder.header("X-Custom-Header", "CustomValue");

                // 변경된 요청으로 교환을 재설정
                ServerHttpRequest request = requestBuilder.build();
                ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();

                // 요청 체인을 계속 진행
                return chain.filter(mutatedExchange);
            };
        }
    }




}
