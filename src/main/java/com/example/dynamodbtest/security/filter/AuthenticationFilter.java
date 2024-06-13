package com.example.dynamodbtest.security.filter;
import com.example.dynamodbtest.security.authentication.CustomReactiveAuthenticationManager;
import com.example.dynamodbtest.user.dto.request.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AuthenticationWebFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomReactiveAuthenticationManager authenticationManager;

    public AuthenticationFilter(CustomReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getRequest().getBody().next().flatMap(dataBuffer -> {
            try {
                LoginDTO loginDTO = objectMapper.readValue(dataBuffer.asInputStream(), LoginDTO.class);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

                return authenticationManager.authenticate(authenticationToken)
                        .flatMap(authentication -> onAuthenticationSuccess(authentication, exchange, chain))
                        .onErrorResume(AuthenticationException.class, e -> onAuthenticationFailure(e, exchange, chain));
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private Mono<Void> onAuthenticationSuccess(Authentication authentication, ServerWebExchange exchange, WebFilterChain chain) {
        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    private Mono<Void> onAuthenticationFailure(AuthenticationException e, ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
