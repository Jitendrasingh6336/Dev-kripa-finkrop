package com.example.dev.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET = "a2VlcFNvbWVUaGluZ1NlY3VyZUFuZERvbnRTaGFyZEluUHVibGlj"; // example base64 string

    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    
    private final long JWT_EXPIRATION = 1000L * 60 * 60 * 24 * 7;


// Generate token
public String generateToken(String email) {
    return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
            .signWith(key)
            .compact();
}

// Extract username
public String extractUsername(String token) {
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
}

// Validate token
public boolean validateToken(String token, String username) {
    String extractedUsername = extractUsername(token);
    return extractedUsername.equals(username) && !isTokenExpired(token);
}

private boolean isTokenExpired(String token) {
    Date expiration = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    return expiration.before(new Date());
}
}
