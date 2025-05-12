package com.trip.treaxure.auth.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";

    public JwtResponse(String token) {
        this.token = token;
    }
    // --- getters & setters ---
    public String getToken() { return token; }
    public void setToken(String t) { this.token = t; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
