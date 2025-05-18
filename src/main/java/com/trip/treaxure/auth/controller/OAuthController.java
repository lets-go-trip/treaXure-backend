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

    // 프론트엔드 메인 페이지 URL
    private static final String FRONTEND_URL = "http://localhost:8080";

    @GetMapping("/kakao/callback")
    public void kakaoCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws Exception {
        String socialToken = kakaoOAuthService.getAccessToken(code);
        Member member = kakaoOAuthService.getUserInfo(socialToken);

        handleLoginResponse(response, member);
    }

    @GetMapping("/naver/callback")
    public void naverCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws Exception {
        String socialToken = naverOAuthService.getAccessToken(code, state);
        Member member = naverOAuthService.getUserInfo(socialToken);

        handleLoginResponse(response, member);
    }

    private void handleLoginResponse(HttpServletResponse response, Member member) {
        String jwtAccessToken = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());

        authService.saveRefreshToken(member.getMemberId().longValue(), jwtRefreshToken);
        
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwtRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenValidity() / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader(HttpHeaders.LOCATION, FRONTEND_URL + "?token=" + jwtAccessToken);
    }
}
