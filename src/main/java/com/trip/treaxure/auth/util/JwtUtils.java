package com.trip.treaxure.auth.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.trip.treaxure.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // secret key를 Base64 인코딩 후 Key로 변환
        byte[] keyBytes = Base64.getEncoder().encode(jwtProperties.getSecret().getBytes());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long memberId) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenValidity()))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long memberId) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenValidity()))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
