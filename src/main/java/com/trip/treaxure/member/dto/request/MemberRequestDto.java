package com.trip.treaxure.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원 가입 요청 DTO")
public class MemberRequestDto {

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "비밀번호", example = "yourPassword123")
    private String password;

    @Schema(description = "닉네임", example = "트레져헌터")
    private String nickname;
}
