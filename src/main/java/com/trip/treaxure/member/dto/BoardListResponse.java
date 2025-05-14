package com.trip.treaxure.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원이 작성한 게시글 목록 응답 DTO")
public class BoardListResponse {
    
    @Schema(description = "사진 목록")
    private List<PhotoDto> photos;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사진 정보 DTO")
    public static class PhotoDto {
        
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
    }
} 