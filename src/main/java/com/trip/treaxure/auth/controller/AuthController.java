package com.trip.treaxure.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.response.JwtResponse;
import com.trip.treaxure.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout() {
        // 인증된 사용자 정보 활용하여 userId 추출 필요
        return ResponseEntity.ok("로그아웃 성공 (토큰 만료 처리)");
    }
}
