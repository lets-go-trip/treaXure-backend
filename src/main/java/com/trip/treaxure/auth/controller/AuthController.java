package com.trip.treaxure.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.request.TokenRefreshRequest;
import com.trip.treaxure.auth.dto.response.TokenRefreshResponse;
import com.trip.treaxure.auth.security.CustomUserDetails;
import com.trip.treaxure.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenRefreshResponse> signin(@RequestBody SignInRequest signInRequest) {
        return authService.signin(signInRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }

    /**
     * 사용자 로그아웃 (Refresh Token 삭제)
     */
    @PostMapping("/signout")
    public ResponseEntity<String> signout() {
        // SecurityContext 로부터 현재 인증정보 획득
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인 사용자만 로그아웃할 수 있습니다.");
        }

        // CustomUserDetails 에서 Member 정보 꺼내기
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getMember().getMemberId().longValue();

        // 서비스 호출: Refresh Token 삭제
        authService.signout(userId);

        // SecurityContext 클리어
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("User signed out successfully!");
    }
}
