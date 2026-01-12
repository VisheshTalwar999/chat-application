package com.example.backend.controller;

import com.example.backend.model.MessageEntity;
import com.example.backend.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService service;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService service,
                          SimpMessagingTemplate messagingTemplate) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    public void send(MessageEntity msg) {

        // ✅ Role / Group based delivery
        if ("ADMIN".equalsIgnoreCase(msg.receiver)) {
            messagingTemplate.convertAndSend("/topic/admin", msg);
        } else {
            messagingTemplate.convertAndSend("/topic/user", msg);
        }

        service.save(msg);
    }
}
