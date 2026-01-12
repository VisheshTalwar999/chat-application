package com.example.backend.util;

import com.example.backend.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final String SECRET = "my-secret-key-my-secret-key";

    public String generate(String email, Role role, long expiry) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString()) //  session id
                .subject(email)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }


    public Claims validate(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getJti(String token) {
        return validate(token).getId();
    }

    public boolean isExpired(String token) {
        Claims claims = validate(token);
        return claims.getExpiration().before(new Date());
    }

}
