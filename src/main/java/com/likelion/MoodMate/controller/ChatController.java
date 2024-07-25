package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/select-model")
    public ModelSelectionResponse selectModel(@RequestBody ModelSelectionRequest request) {
        return chatService.selectModel(request);
    }

    @PostMapping("/chat")
    public ChatResponse sendMessage(@RequestBody ChatRequest request) {
        return chatService.sendMessage(request);
    }
}
