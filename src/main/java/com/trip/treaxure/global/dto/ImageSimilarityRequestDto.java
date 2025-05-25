package com.trip.treaxure.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "이미지 유사도 요청 DTO")
public class ImageSimilarityRequestDto {
    
    @Schema(description = "기준 이미지 URL", example = "https://example.com/reference.jpg")
    private String referenceImageUrl;
    
    @Schema(description = "비교 대상 이미지 URL", example = "https://example.com/target.jpg")
    private String targetImageUrl;
} 