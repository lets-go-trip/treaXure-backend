package com.trip.treaxure.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.auth.dto.response.JwtResponse;
import com.trip.treaxure.auth.service.AuthService;
import com.trip.treaxure.auth.service.KakaoOAuthService;
import com.trip.treaxure.auth.service.NaverOAuthService;
import com.trip.treaxure.auth.util.JwtUtils;
import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.member.entity.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final NaverOAuthService naverOAuthService;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponseDto<JwtResponse>> kakaoCallback(@RequestParam("code") String code) throws Exception {
        String accessToken = kakaoOAuthService.getAccessToken(code);
        Member member = kakaoOAuthService.getUserInfo(accessToken);

        String jwtAccessToken = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());

        authService.saveRefreshToken(member.getMemberId().longValue(), jwtRefreshToken);

        JwtResponse jwtResponse = new JwtResponse(jwtAccessToken, jwtRefreshToken);
        return ResponseEntity.ok(ApiResponseDto.success(jwtResponse));
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<ApiResponseDto<JwtResponse>> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception {
        String accessToken = naverOAuthService.getAccessToken(code, state);
        Member member = naverOAuthService.getUserInfo(accessToken);

        String jwtAccessToken = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());
        authService.saveRefreshToken(member.getMemberId().longValue(), jwtRefreshToken);

        JwtResponse jwtResponse = new JwtResponse(jwtAccessToken, jwtRefreshToken);
        return ResponseEntity.ok(ApiResponseDto.success(jwtResponse));
    }

}
