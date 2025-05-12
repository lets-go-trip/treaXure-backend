package com.trip.treaxure.auth.service;

import com.trip.treaxure.auth.dto.LoginRequest;
import com.trip.treaxure.auth.dto.SignupRequest;
import com.trip.treaxure.auth.dto.JwtResponse;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.entity.Member.MemberRole;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public ResponseEntity<?> register(SignupRequest req) {
        if (memberRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email already in use!");
        }
        if (memberRepository.findByNickname(req.getNickname()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Nickname already taken!");
        }
        Member m = new Member();
        m.setEmail(req.getEmail());
        m.setPassword(passwordEncoder.encode(req.getPassword()));
        m.setNickname(req.getNickname());
        m.setRole(MemberRole.USER);
        memberRepository.save(m);
        return ResponseEntity.ok("User registered successfully!");
    }

    public ResponseEntity<JwtResponse> login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = jwtUtil.generateToken(auth);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
