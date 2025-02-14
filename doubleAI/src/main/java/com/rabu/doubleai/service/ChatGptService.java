package com.rabu.doubleai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {

    @Value("${openai.key}")
    private String API_KEY;  // OpenAI API Key

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatGptResponse(String userInput) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            throw new IllegalStateException("OpenAI API Key가 설정되지 않았습니다.");
        }

        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY.substring(1, API_KEY.length() - 2));
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // 메시지 생성
            Map<String, Object> systemMessage = Map.of(
                    "role", "system",
                    "content", "당신은 유용한 정보를 제공하는 친절한 AI 챗봇입니다. 질문에 대해 항상 자세하고 정확하며 창의적인 한국어 답변을 제공합니다."
            );

            Map<String, Object> userMessage = Map.of(
                    "role", "user",
                    "content", userInput
            );

            // 요청 본문 객체 생성
            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(systemMessage, userMessage)
            );

            // Jackson을 사용하여 객체를 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyString = objectMapper.writeValueAsString(requestBody);

            // HTTP 요청 생성
            HttpEntity<String> entity = new HttpEntity<>(requestBodyString, headers);

            // OpenAI API 호출
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "OpenAI API 호출 중 오류 발생: " + e.getMessage();
        }
    }
}
