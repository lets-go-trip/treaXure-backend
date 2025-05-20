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
@Schema(description = "Presigned URL 발급 요청 DTO")
public class PresignedUrlRequest {
    @Schema(description = "사용자 닉네임", example = "user123", required = true)
    private String userNickname;
    
    @Schema(description = "업로드할 파일명(확장자 포함)", example = "profile.webp", required = true)
    private String fileName;
    
    @Schema(description = "파일의 MIME 타입", example = "image/webp", required = true)
    private String contentType;
} 