package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserCouchDoc user) {
        auth.register(user);
        return "Registration request sent to admin";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return auth.login(req);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String header) {

        String token = header.substring(7);
        auth.logout(token);
        return "Logged out successfully";
    }

}
