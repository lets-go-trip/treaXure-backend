package com.trip.treaxure.auth.controller;

import com.trip.treaxure.auth.dto.LoginRequest;
import com.trip.treaxure.auth.dto.SignupRequest;
import com.trip.treaxure.auth.dto.JwtResponse;
import com.trip.treaxure.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
