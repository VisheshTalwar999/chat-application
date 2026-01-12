package com.example.backend.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();

    public void create(String email, String token) {
        sessions.put(email, token);
    }

    public boolean exists(String email) {
        return sessions.containsKey(email);
    }

    public boolean isValid(String email, String token) {
        return token.equals(sessions.get(email));
    }

    public void remove(String email) {
        sessions.remove(email);
    }
}
