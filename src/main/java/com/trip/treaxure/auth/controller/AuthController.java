package com.trip.treaxure.auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

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

    private Long getUserIdFromJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new IllegalArgumentException("Authorization header is missing or malformed.");
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        String encoded = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
        response.setHeader("Set-Cookie", "refreshToken=" + encoded + "; HttpOnly; Path=/; Secure; SameSite=None");
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        response.setHeader("Set-Cookie", "refreshToken=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");
    }
}