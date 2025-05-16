package com.trip.treaxure.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trip.treaxure.auth.service.AuthService;
import com.trip.treaxure.auth.service.KakaoOAuthService;
import com.trip.treaxure.auth.service.NaverOAuthService;
import com.trip.treaxure.auth.util.JwtUtils;
import com.trip.treaxure.config.JwtProperties;
import com.trip.treaxure.member.entity.Member;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final NaverOAuthService naverOAuthService;
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    // 리다이렉트할 프론트엔드 URL
    private static final String FRONTEND_URL = "http://localhost:8080";

    @GetMapping("/kakao/callback")
    public void kakaoCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws Exception {
        // 1) 카카오에서 받은 code로 소셜 액세스 토큰 & 유저 정보 조회
        String socialToken = kakaoOAuthService.getAccessToken(code);
        Member member     = kakaoOAuthService.getUserInfo(socialToken);

        // 2) 우리 시스템용 JWT 발급 및 리프레시 토큰 저장
        String jwtAccessToken  = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());
        authService.saveRefreshToken(member.getMemberId().longValue(), jwtRefreshToken);

        // 3) HTTP-Only, Secure 쿠키로 세팅
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", jwtAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getAccessTokenValidity() / 1000)
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", jwtRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenValidity() / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 4) 프론트엔드 메인으로 Redirect
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader(HttpHeaders.LOCATION, FRONTEND_URL);
    }

    @GetMapping("/naver/callback")
    public void naverCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws Exception {
        // 1) 네이버에서 받은 code, state로 소셜 액세스 토큰 & 유저 정보 조회
        String socialToken = naverOAuthService.getAccessToken(code, state);
        Member member     = naverOAuthService.getUserInfo(socialToken);

        // 2) 우리 시스템용 JWT 발급 및 리프레시 토큰 저장
        String jwtAccessToken  = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());
        authService.saveRefreshToken(member.getMemberId().longValue(), jwtRefreshToken);

        // 3) HTTP-Only, Secure 쿠키로 세팅
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", jwtAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getAccessTokenValidity() / 1000)
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", jwtRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenValidity() / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 4) 프론트엔드 메인으로 Redirect
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader(HttpHeaders.LOCATION, FRONTEND_URL);
    }
}
