package com.example.dynamodbtest.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("example_route", r -> r.path("/api/**")
//                        .filters(f -> f.filter(new AuthenticationFilter()))
                        .uri("https://server.greenseed.or.kr")) // 다른 서버의 URI
                .build();
    }
}
