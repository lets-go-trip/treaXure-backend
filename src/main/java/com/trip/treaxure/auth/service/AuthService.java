package com.trip.treaxure.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.treaxure.auth.dto.request.SignInRequest;
import com.trip.treaxure.auth.dto.request.SignUpRequest;
import com.trip.treaxure.auth.dto.request.TokenRefreshRequest;
import com.trip.treaxure.auth.dto.response.TokenRefreshResponse;
import com.trip.treaxure.auth.entity.RefreshToken;
import com.trip.treaxure.auth.repository.RefreshTokenRepository;
import com.trip.treaxure.auth.security.CustomUserDetails;
import com.trip.treaxure.auth.security.JwtUtil;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    /** 회원가입 */
    public ResponseEntity<?> register(SignUpRequest req) {
        if (memberRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email already in use!");
        }
        if (memberRepository.findByNickname(req.getNickname()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Nickname already taken!");
        }
        Member user = new Member();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setRole(Member.MemberRole.USER);
        memberRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    /** 로그인(Access + Refresh Token 발급) */
    @Transactional
    public ResponseEntity<TokenRefreshResponse> signin(SignInRequest req) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getMember().getMemberId().longValue();

        // Access Token 생성
        String accessToken = jwtUtil.generateToken(authentication);
        // Refresh Token 생성 및 저장
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);

        return ResponseEntity.ok(
            new TokenRefreshResponse(accessToken, refreshToken.getToken())
        );
    }

    /** Access Token 재발급 */
    public ResponseEntity<TokenRefreshResponse> refreshToken(TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();
        RefreshToken storedToken = refreshTokenRepository.findByToken(requestToken)
            .orElseThrow(() -> new RuntimeException("Refresh token not found."));
        // 만료 여부 검증
        refreshTokenService.verifyExpiration(storedToken);

        // 새로운 Access Token 발급
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                storedToken.getUser().getEmail(),
                null,
                userDetailsAuthorities(storedToken.getUser())
            );
        String newAccessToken = jwtUtil.generateToken(auth);

        return ResponseEntity.ok(
            new TokenRefreshResponse(newAccessToken, requestToken)
        );
    }

    /** 로그아웃(Refresh Token 삭제) */
    public void signout(Long userId) {
        refreshTokenService.deleteByUserId(userId);
    }

    // Helper: Member → GrantedAuthority 목록 생성
    private java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority>
        userDetailsAuthorities(Member user) {
        return java.util.List.of(
            new org.springframework.security.core.authority.SimpleGrantedAuthority(
                "ROLE_" + user.getRole().name()
            )
        );
    }
}
