package com.trip.treaxure.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "서명된 URL 발급 요청 DTO")
public class SignedUrlRequest {
    @Schema(description = "S3에 저장된 객체 키(경로)", example = "images/missions/user123/1234567890-abcdef.webp", required = true)
    private String objectKey;
} 