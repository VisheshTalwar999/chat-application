package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.lightcouch.CouchDbClient;
import org.springframework.stereotype.Service;
import com.example.backend.websocket.ChatSocketHandler;

import java.util.List;

@Service
public class AuthService {

    private final CouchDbClient couch;
    private final JwtUtil jwtUtil;
//    private final SessionService sessionService;

    public AuthService(CouchDbClient couch, JwtUtil jwtUtil) {
        this.couch = couch;
        this.jwtUtil = jwtUtil;
    }

    public void register(UserCouchDoc user) {
        user.setStatus(UserStatus.PENDING);
        couch.save(user);
    }


    public LoginResponse login(LoginRequest req) {

        List<UserCouchDoc> users = couch
                .view("users/byEmail")
                .key(req.email)
                .includeDocs(true)
                .query(UserCouchDoc.class);

        if (users.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserCouchDoc user = users.get(0);

        if (user.getStatus() != UserStatus.APPROVED) {
            throw new RuntimeException("User not approved by admin");
        }

        if (!user.getPassword().equals(req.password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // ✅ SINGLE LOGIN CHECK (DB based)
        if (user.getActiveToken() != null) {
            throw new RuntimeException("User already logged in on another device.");
        }

        long expiry = user.getRole() == Role.ADMIN ? 20_000 : 1_800_000;

        String token = jwtUtil.generate(user.getEmail(), user.getRole(), expiry);
//        sessionService.create(user.getEmail(), token);
        // ✅ Store token in CouchDB
        user.setActiveToken(token);
        couch.update(user);

        return new LoginResponse(token, user.getRole());
    }

    public void logout(String token) {
        Claims claims = jwtUtil.validate(token);
        String email = claims.getSubject();

        UserCouchDoc user = couch
                .view("users/byEmail")
                .key(email)
                .includeDocs(true)
                .query(UserCouchDoc.class)
                .get(0);

        // ✅ CLEAR ACTIVE TOKEN
        user.setActiveToken(null);
        couch.update(user);

        // ✅ Disconnect WebSocket connections
        ChatSocketHandler.disconnectAll();
    }


}
