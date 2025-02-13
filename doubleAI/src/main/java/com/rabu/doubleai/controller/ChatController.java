package com.rabu.doubleai.controller;

import com.rabu.doubleai.model.ChatRequest;
import com.rabu.doubleai.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;

    @Autowired
    public ChatController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/ask")
    public String askChatGpt(@RequestBody ChatRequest request) {
        return chatGptService.getChatGptResponse(request.getInput());
    }
}
