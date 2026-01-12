package com.example.backend.model;

public class LoginResponse {
    public String token;
    public Role role;

    public LoginResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }
}

