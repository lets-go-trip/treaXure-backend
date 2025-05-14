package com.trip.treaxure.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.response.JwtResponse;
import com.trip.treaxure.auth.jwt.JwtTokenProvider;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse signin(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getMemberId().longValue());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId().longValue());

        return new JwtResponse(accessToken, refreshToken);
    }

    public void signup(SignUpRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setNickname(request.getNickname());
        memberRepository.save(member);
    }

    public void signout(Long userId) {
        // RefreshToken 저장소 사용 시, 해당 토큰을 제거
        // 지금은 저장소 없으므로 생략
    }
}
