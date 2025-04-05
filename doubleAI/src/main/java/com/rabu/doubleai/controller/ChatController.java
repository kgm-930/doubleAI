package com.rabu.doubleai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabu.doubleai.model.ChatRequest;
import com.rabu.doubleai.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱 객체

    @Autowired
    public ChatController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askChatGpt(@RequestBody ChatRequest request) {
        // 요청이 올바른지 검증
        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: message is missing"));
        }

        System.out.println("요청 메시지: " + request.getInput());

        try {
            String rawResponse = chatGptService.getChatGptResponse(request.getInput());
            System.out.println("Raw OpenAI Response: " + rawResponse);

            // JSON 응답을 파싱하여 message.content 추출
            JsonNode rootNode = objectMapper.readTree(rawResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (!choicesNode.isArray() || choicesNode.isEmpty()) {
                return ResponseEntity.status(500).body(Map.of("error", "Invalid OpenAI response format"));
            }

            String messageContent = choicesNode.get(0).path("message").path("content").asText();

            // 올바른 JSON 응답 생성
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("response", messageContent);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "JSON parsing error"));
        }
    }
    
}
