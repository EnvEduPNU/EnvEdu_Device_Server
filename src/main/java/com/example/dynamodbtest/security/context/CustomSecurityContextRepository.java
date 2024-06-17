//package com.example.dynamodbtest.security.context;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextImpl;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.server.context.ServerSecurityContextRepository;
//import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CustomSecurityContextRepository implements ServerSecurityContextRepository {
//
//    private final ReactiveAuthenticationManager authenticationManager;
//
//    @Override
//    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
//        // 커스텀 로직으로 보안 컨텍스트를 저장하는 로직을 구현하거나, 기본 구현을 사용할 수 있습니다.
//        return new WebSessionServerSecurityContextRepository().save(exchange, context);
//    }
//
//    @Override
//    public Mono<SecurityContext> load(ServerWebExchange exchange) {
//
//        log.info("토큰 확인 및 설정");
//        // 요청에서 토큰을 추출하여 인증 절차를 진행하고, 결과로 SecurityContext를 반환합니다.
//        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
//                .map(authHeader -> authHeader.replace("Bearer ", ""))
//                .flatMap(token -> authenticate(token))
//                .map(authentication -> new SecurityContextImpl(authentication));
//    }
//
//    private Mono<Authentication> authenticate(String token) {
//        return Mono.just(new UsernamePasswordAuthenticationToken(token, token))
//                .flatMap(authentication -> authenticationManager.authenticate(authentication));
//    }
//}
