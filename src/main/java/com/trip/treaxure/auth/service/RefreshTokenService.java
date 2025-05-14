package com.trip.treaxure.auth.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.trip.treaxure.auth.entity.RefreshToken;
import com.trip.treaxure.auth.repository.RefreshTokenRepository;
import com.trip.treaxure.member.repository.MemberRepository;

@Service
public class RefreshTokenService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepo;
    private final MemberRepository memberRepo;

    public RefreshTokenService(RefreshTokenRepository rtr, MemberRepository mr) {
        this.refreshTokenRepo = rtr;
        this.memberRepo = mr;
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
            .user(memberRepo.findById(userId).get())
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
            .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.delete(token);
            throw new RuntimeException("Refresh token was expired. Please signin again.");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        memberRepo.findById(userId).ifPresent(user -> refreshTokenRepo.deleteByUser(user));
    }
}
