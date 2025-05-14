package com.trip.treaxure.auth.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Refresh Token을 메모리에 저장하는 임시 저장소 (개발용).
 * 실제 운영 환경에서는 Redis 또는 DB를 사용하는 것이 안전합니다.
 */
@Component
public class RefreshTokenStore {

    private final Map<Long, String> refreshTokens = new ConcurrentHashMap<>();

    public void save(Long memberId, String token) {
        refreshTokens.put(memberId, token);
    }

    public String get(Long memberId) {
        return refreshTokens.get(memberId);
    }

    public void remove(Long memberId) {
        refreshTokens.remove(memberId);
    }

    public boolean validate(Long memberId, String refreshToken) {
        return refreshToken.equals(refreshTokens.get(memberId));
    }
}
