package com.trip.treaxure.member.dto.response;

import java.time.LocalDateTime;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.entity.Member.MemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 응답 DTO")
public class MemberResponseDto {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "트레져헌터")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileUrl;

    @Schema(description = "사용자 포인트", example = "0")
    private Integer point;
    
    @Schema(description = "권한", example = "USER")
    private MemberRole role;

    @Schema(description = "생성일", example = "2025-05-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2025-05-02T00:00:00")
    private LocalDateTime updatedAt;

    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .role(member.getRole())
                .point(member.getPoint())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
