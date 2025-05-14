package com.trip.treaxure.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 정보 수정 요청 DTO")
public class MemberUpdateRequestDto {

    @Schema(description = "닉네임", example = "트레져헌터")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    private String profileUrl;
}
