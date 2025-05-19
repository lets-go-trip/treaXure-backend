package com.trip.treaxure.auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.trip.treaxure.auth.service.AuthService;
import com.trip.treaxure.auth.util.JwtUtils;
import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.member.dto.request.MemberUpdateRequestDto;
import com.trip.treaxure.member.dto.response.MemberResponseDto;
import com.trip.treaxure.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto<String>> signin(@RequestBody SignInRequest request, HttpServletResponse response) {
        JwtResponse jwt = authService.signin(request);
        addRefreshTokenToCookie(response, jwt.getRefreshToken());
        return ResponseEntity.ok(ApiResponseDto.success(jwt.getAccessToken()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<String>> signup(@RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponseDto.success("회원가입 완료"));
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponseDto<String>> signout(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getUserIdFromJwt(request);
        authService.signout(userId);
        deleteRefreshTokenCookie(response);
        return ResponseEntity.ok(ApiResponseDto.success("로그아웃 완료"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        JwtResponse jwt = authService.refresh(request);
        return ResponseEntity.ok(ApiResponseDto.success(jwt));
    }

    @Operation(summary = "내 정보 조회", description = "헤더의 Bearer 토큰으로 내 회원 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<MemberResponseDto>> getMyInfo(HttpServletRequest request) {
        Long userId = getUserIdFromJwt(request);
        MemberResponseDto me = memberService.getMemberById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return ResponseEntity.ok(ApiResponseDto.success(me));
    }

    @Operation(summary = "내 정보 수정", description = "헤더의 Bearer 토큰으로 내 회원 정보를 수정합니다.")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto<MemberResponseDto>> updateMyInfo(HttpServletRequest request, @RequestBody MemberUpdateRequestDto dto) {
        Long userId = getUserIdFromJwt(request);
        MemberResponseDto updated = memberService.updateMember(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success(updated));
    }

    @Operation(summary = "내 계정 비활성화", description = "헤더의 Bearer 토큰으로 본인 계정을 soft-delete 형태로 비활성화합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponseDto<String>> deactivateMyAccount(HttpServletRequest request) {
        Long userId = getUserIdFromJwt(request);
        memberService.deactivateMember(userId);
        return ResponseEntity.ok(ApiResponseDto.success("계정이 비활성화되었습니다."));
    }

    private Long getUserIdFromJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new IllegalArgumentException("존재하지 않는 회원입니다.");
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        String encoded = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
        response.setHeader("Set-Cookie", "refreshToken=" + encoded + "; HttpOnly; Path=/; Secure; SameSite=None");
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        response.setHeader("Set-Cookie", "refreshToken=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");
    }
}