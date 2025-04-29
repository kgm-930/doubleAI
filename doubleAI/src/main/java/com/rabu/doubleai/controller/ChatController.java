package com.rabu.doubleai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabu.doubleai.model.ChatRequest;
import com.rabu.doubleai.model.User;
import com.rabu.doubleai.model.ChatHistory;
import com.rabu.doubleai.repository.UserRepository;
import com.rabu.doubleai.service.ChatGptService;
import com.rabu.doubleai.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;
    private final ChatHistoryService chatHistoryService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ChatController(ChatGptService chatGptService,
                          ChatHistoryService chatHistoryService,
                          UserRepository userRepository) {
        this.chatGptService = chatGptService;
        this.chatHistoryService = chatHistoryService;
        this.userRepository = userRepository;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askChatGpt(@RequestBody ChatRequest request, Principal principal) {
        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: message is missing"));
        }

        try {
            String rawResponse = chatGptService.getChatGptResponse(request.getInput());
            JsonNode rootNode = objectMapper.readTree(rawResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (!choicesNode.isArray() || choicesNode.isEmpty()) {
                return ResponseEntity.status(500).body(Map.of("error", "Invalid OpenAI response format"));
            }

            String messageContent = choicesNode.get(0).path("message").path("content").asText();

            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                chatHistoryService.saveChat(user, request.getInput(), messageContent); // ✅ 대화 저장
            }

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("response", messageContent));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "JSON parsing error"));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, String>>> getChatHistory(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();

        List<ChatHistory> historyList = chatHistoryService.getHistory(user);

        List<Map<String, String>> response = historyList.stream()
                .map(h -> Map.of("question", h.getQuestion(), "answer", h.getAnswer()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteChatHistory(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        chatHistoryService.deleteHistory(user);
        return ResponseEntity.ok().build();
    }
}
