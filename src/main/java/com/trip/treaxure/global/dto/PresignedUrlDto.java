package com.trip.treaxure.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Presigned URL 발급 응답 DTO")
public class PresignedUrlDto {
    @Schema(description = "S3에 직접 업로드할 수 있는 URL", example = "https://bucket-name.s3.ap-northeast-2.amazonaws.com/images/missions/file.webp?X-Amz-Algorithm=...", required = true)
    private String presignedUrl;
    
    @Schema(description = "S3에 저장될 객체 키(경로)", example = "images/missions/user123/1234567890-abcdef.webp", required = true)
    private String originalObjectKey;
    
    @Schema(description = "생성될 썸네일 이미지의 객체 키(경로)", example = "images/missions/user123/1234567890-abcdef_thumbnail.webp", required = true)
    private String thumbnailObjectKey;
    
    @Schema(description = "S3 버킷 이름", example = "treaxure-images", required = true)
    private String bucketName;
} 