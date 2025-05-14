package com.trip.treaxure.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.auth.entity.RefreshToken;
import com.trip.treaxure.member.entity.Member;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(Member user);
}
