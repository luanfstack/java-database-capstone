package com.project.back_end.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // 7 days

        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
    }

    public String extractIdentifier(String token) {
        try {
            return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token);
            return identifier != null && identifier.equals(user);
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
