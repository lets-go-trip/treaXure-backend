package com.trip.treaxure.auth.dto;

public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    // --- getters & setters ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String pw) { this.password = pw; }
    public String getNickname() { return nickname; }
    public void setNickname(String nick) { this.nickname = nick; }
}
