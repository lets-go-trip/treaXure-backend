package com.trip.treaxure.auth.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trip.treaxure.auth.dto.request.RefreshTokenRequest;
import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.response.JwtResponse;
import com.trip.treaxure.auth.util.JwtUtils;
import com.trip.treaxure.auth.util.RefreshTokenStore;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final RefreshTokenStore refreshTokenStore;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 처리 및 토큰 발급
     */
    public JwtResponse signin(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

        // 계정이 비활성화된 경우 로그인 차단
        if (!member.getIsActive()) {
            throw new IllegalStateException("비활성화된 사용자입니다. 다시 로그인해주세요.");
        }

        // 비밀번호 검사
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtils.generateAccessToken(member.getMemberId().longValue());
        String refreshToken = jwtUtils.generateRefreshToken(member.getMemberId().longValue());

        refreshTokenStore.save(member.getMemberId().longValue(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * 회원가입 처리
     */
    public void signup(SignUpRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member newMember = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(Member.MemberRole.USER)
                .isActive(true)
                .build();

        memberRepository.save(newMember);
    }

    /**
     * 로그아웃 처리 (Refresh Token 제거)
     */
    public void signout(Long memberId) {
        refreshTokenStore.remove(memberId);
    }

    /**
     * Refresh
     */
    public JwtResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtils.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        Long memberId = jwtUtils.getUserIdFromToken(refreshToken);

        // 저장소에 존재하는지 확인
        String stored = refreshTokenStore.get(memberId);
        if (stored == null || !stored.equals(refreshToken)) {
            throw new SecurityException("저장된 Refresh Token과 일치하지 않습니다.");
        }

        // 새로운 Access Token 발급
        String newAccessToken = jwtUtils.generateAccessToken(memberId);

        return new JwtResponse(newAccessToken, refreshToken); // refresh는 재발급하지 않음
    }

    // AuthService 클래스 내부에 추가
    public void saveRefreshToken(Long memberId, String refreshToken) {
        refreshTokenStore.save(memberId, refreshToken);
    }

}
