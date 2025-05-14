package com.trip.treaxure.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 프로필 응답 DTO")
public class MemberProfileResponse {
    
    @Schema(description = "회원 ID", example = "123")
    private Integer memberId;
    
    @Schema(description = "이메일", example = "member@example.com")
    private String email;
    
    @Schema(description = "닉네임", example = "Explorer")
    private String nickname;
    
    @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/123.jpg")
    private String profileUrl;
    
    @Schema(description = "총 점수", example = "450")
    private Integer totalScore;
    
    @Schema(description = "방문 장소 수", example = "12")
    private Integer visitedCount;
} 