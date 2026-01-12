package com.example.backend.service;

import com.example.backend.model.MessageEntity;
import com.example.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public MessageEntity save(MessageEntity msg) {
        msg.timestamp = LocalDateTime.now();
        return repo.save(msg);
    }
}
