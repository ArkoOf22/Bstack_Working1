package com.example.browsertry2.controller;

import com.example.browsertry2.models.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class webSocketController {
    @MessageMapping("/logs")
    @SendTo("/topic/log")
    public Message getFileContent(Message message) {
        return message;
    }
}
