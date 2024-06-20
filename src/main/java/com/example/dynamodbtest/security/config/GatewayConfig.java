package com.example.dynamodbtest.security.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
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
                .route("api_route", r -> r.path("/login/**", "/seed/**", "/mydata/**", "/datafolder/**", "/api/**")
                        .filters(f -> f.modifyRequestBody(String.class, String.class, (exchange, s) -> {
                            return exchange.getRequest().getBody()
                                    .map(dataBuffer -> {
                                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(bytes);
                                        DataBufferUtils.release(dataBuffer); // 데이터 버퍼 해제
                                        return new String(bytes, StandardCharsets.UTF_8);
                                    })
                                    .reduce("", (prev, content) -> prev + content) // 내용을 하나의 문자열로 결합
                                    .map(body -> {
                                        // 변형된 본문 데이터를 exchange 속성에 저장
                                        exchange.getAttributes().put("modifiedBody", body);
                                        return body;
                                    });
                        }))
                        .uri("https://server.greenseed.or.kr")) // API 요청을 처리하는 서버 URI

                .route("websocket_route", r -> r.path("/ws/**")
                        .uri("https://server.greenseed.or.kr")) // WebSocket 요청을 처리하는 서버 URI

                .route("screen_share_route", r -> r.path("/screen-share/**")
                        .uri("https://server.greenseed.or.kr")) // 화면 공유 요청을 처리하는 서버 URI

                .build();
    }








}
