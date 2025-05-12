package com.trip.treaxure.auth.dto;

public class LoginRequest {
    private String email;
    private String password;
    // --- getters & setters ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String pw) { this.password = pw; }
}
