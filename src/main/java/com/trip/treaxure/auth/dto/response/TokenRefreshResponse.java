package com.trip.treaxure.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
