package com.khrystoforov.onlineshopapp.security;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTTokenProvider {
    private final String secret;
    private final Long expirationTime;

    public JWTTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") Long expirationTime) {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expirationTime);
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId() + "");
        claims.put("role", user.getRole());
        claims.put("name", user.getName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public User parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();
            user.setEmail(body.getSubject());
            user.setName((String) body.get("name"));
            user.setId(Long.parseLong((String) body.get("userId")));
            user.setRole(Role.getRole((String) body.get("role")));

            return user;
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }
}