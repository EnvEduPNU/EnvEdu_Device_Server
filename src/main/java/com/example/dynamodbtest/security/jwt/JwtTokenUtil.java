package com.example.dynamodbtest.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static String secret = null;
    private SecretKey secretKey;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // 역할 정보를 클레임으로 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 72000000)) // 20시간 유효 기간
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            // 만료 시간 확인
            if (claims.getExpiration().before(new Date())) {
                throw new RuntimeException("JWT token is expired");
            }

            return claims;

        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public static Claims parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }
}
