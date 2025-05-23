package com.trip.treaxure.global.controller;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.global.dto.ImageSimilarityRequestDto;
import com.trip.treaxure.global.dto.ImageSimilarityResponseDto;
import com.trip.treaxure.global.service.ImageSimilarityService;
import com.trip.treaxure.global.service.OpenAiVisionSimilarityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/image-similarity")
@Tag(name = "Image Similarity", description = "이미지 유사도 평가 API")
@RequiredArgsConstructor
public class ImageSimilarityController {

    private final OpenAiVisionSimilarityService openAiVisionSimilarityService;

    @Operation(summary = "이미지 유사도 평가", description = "두 이미지 URL 간의 유사도를 평가합니다. 0에서 1 사이의 점수를 반환합니다(1에 가까울수록 유사).")
    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponseDto<ImageSimilarityResponseDto>> evaluateImageSimilarity(
            @RequestBody ImageSimilarityRequestDto requestDto) {
        
        log.info("Evaluating similarity between {} and {}", 
                requestDto.getReferenceImageUrl(), requestDto.getTargetImageUrl());
        
        float similarityScore = openAiVisionSimilarityService.compare(
                requestDto.getReferenceImageUrl(), 
                requestDto.getTargetImageUrl());
        
        ImageSimilarityResponseDto responseDto = ImageSimilarityResponseDto.builder()
                .referenceImageUrl(requestDto.getReferenceImageUrl())
                .targetImageUrl(requestDto.getTargetImageUrl())
                .similarityScore(similarityScore)
                .build();
        
        return ResponseEntity.ok(ApiResponseDto.success(responseDto));
    }
} 