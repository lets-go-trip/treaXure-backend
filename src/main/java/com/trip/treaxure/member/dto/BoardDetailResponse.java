package com.trip.treaxure.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 상세 정보 응답 DTO")
public class BoardDetailResponse {
    
    @Schema(description = "사진 ID", example = "301")
    private Integer photoId;
    
    @Schema(description = "미션 ID", example = "101")
    private Integer missionId;
    
    @Schema(description = "이미지 URL", example = "https://cdn.example.com/photos/301.jpg")
    private String imageUrl;
    
    @Schema(description = "유사도 점수", example = "0.87")
    private Double similarityScore;
    
    @Schema(description = "상태", example = "SELECTED")
    private String status;
    
    @Schema(description = "업로드 시간", example = "2025-05-07T14:45:00")
    private LocalDateTime uploadedAt;
    
    @Schema(description = "획득 점수", example = "20")
    private Integer scoreAwarded;
    
    @Schema(description = "평가 시간", example = "2025-05-08T10:00:00")
    private LocalDateTime evaluatedAt;
    
    @Schema(description = "피드백", example = "우수한 사진입니다!")
    private String feedback;
} 