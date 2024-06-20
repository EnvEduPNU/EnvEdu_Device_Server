package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
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
                .route("example_route", r -> r.path("/login/**","/seed/**","/mydata/**","/datafolder/**","/api/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            ServerHttpRequest request = exchange.getRequest();
                            HttpHeaders headers = request.getHeaders();

                            // 기존 요청 헤더 복사
                            ServerHttpRequest.Builder requestBuilder = request.mutate();
                            headers.forEach((key, values) -> values.forEach(value -> requestBuilder.header(key, value)));

                            // 요청 본문 복사
                            Flux<DataBuffer> body = request.getBody().map(dataBuffer -> {
                                DataBufferUtils.retain(dataBuffer);
                                return dataBuffer;
                            });

                            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(requestBuilder.build()) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return body;
                                }
                            };

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
