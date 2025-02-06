package com.rabu.doubleai.controller;

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

    // 사용자가 입력한 텍스트를 받아 OpenAI에 전달하고 응답을 반환
    @PostMapping("/ask")
    public String askChatGpt(@RequestBody String userInput) {
        return chatGptService.getChatGptResponse(userInput);
    }
}
