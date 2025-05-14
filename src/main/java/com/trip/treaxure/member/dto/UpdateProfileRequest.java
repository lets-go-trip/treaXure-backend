package com.trip.treaxure.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 프로필 수정 요청 DTO")
public class UpdateProfileRequest {
    
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Schema(description = "변경할 닉네임", example = "NewExplorer")
    private String nickname;
    
    @Schema(description = "변경할 프로필 URL", example = "https://cdn.example.com/profiles/new.jpg")
    private String profileUrl;
} 