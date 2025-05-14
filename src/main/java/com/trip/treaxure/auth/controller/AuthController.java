package com.trip.treaxure.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.auth.dto.request.RefreshTokenRequest;
import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.response.JwtResponse;
import com.trip.treaxure.auth.security.CustomUserDetails;
import com.trip.treaxure.auth.service.AuthService;
import com.trip.treaxure.auth.util.JwtUtils;
import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.member.dto.request.MemberUpdateRequestDto;
import com.trip.treaxure.member.dto.response.MemberResponseDto;
import com.trip.treaxure.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto<JwtResponse>> signin(@RequestBody SignInRequest request) {
        JwtResponse jwt = authService.signin(request);
        return ResponseEntity.ok(ApiResponseDto.success(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<String>> signup(@RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponseDto.success("회원가입 완료"));
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponseDto<String>> signout(HttpServletRequest request) {
        Long userId = getUserIdFromJwt(request);
        authService.signout(userId);
        return ResponseEntity.ok(ApiResponseDto.success("로그아웃 완료"));
    }

    @Autowired
    private JwtUtils jwtUtils;

    private Long getUserIdFromJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new IllegalArgumentException("Authorization header is missing or malformed.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Access Token 재발급", description = "유효한 Refresh Token으로 새로운 Access Token을 발급합니다.")
    public ResponseEntity<ApiResponseDto<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        JwtResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    private final MemberService memberService;


    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<MemberResponseDto>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId().longValue();

        MemberResponseDto responseDto = MemberResponseDto.fromEntity(
                memberService.getEntityById(memberId)
        );

        return ResponseEntity.ok(ApiResponseDto.success(responseDto));
    }

    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 닉네임, 프로필을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
        @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto<MemberResponseDto>> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MemberUpdateRequestDto dto) {

        Long memberId = userDetails.getMember().getMemberId().longValue();
        MemberResponseDto updated = memberService.updateMember(memberId, dto);
        return ResponseEntity.ok(ApiResponseDto.success(updated));
    }

}
