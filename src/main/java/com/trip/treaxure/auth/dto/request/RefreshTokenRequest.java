package com.trip.treaxure.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Refresh Token 요청 DTO")
public class RefreshTokenRequest {

    @Schema(description = "Refresh Token", required = true)
    private String refreshToken;
}
