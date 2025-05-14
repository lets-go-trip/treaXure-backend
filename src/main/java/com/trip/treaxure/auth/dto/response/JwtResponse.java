package com.trip.treaxure.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "JWT 로그인 응답 DTO")
public class JwtResponse {

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUz...")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUz...")
    private String refreshToken;

    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType = "Bearer";

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }
}
