package com.example.dynamodbtest.security.authentication;

import com.example.dynamodbtest.security.principal.CustomUserDetails;
import com.example.dynamodbtest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username))) // 아이디가 없을 때 예외 처리
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        CustomUserDetails userDetails = new CustomUserDetails(user);
                        return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
                    } else {
                        return Mono.error(new BadCredentialsException("Invalid credentials"));
                    }
                });
    }

}
