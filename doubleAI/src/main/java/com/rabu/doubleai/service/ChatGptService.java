package com.rabu.doubleai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class ChatGptService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatGptResponse(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openAiApiKey.substring(1,openAiApiKey.length()-2));

            // 요청 바디 생성
            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", new Object[]{Map.of("role", "user", "content", userMessage)},
                    "max_tokens", 2048
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // OpenAI API 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, request, String.class);

            return response.getBody(); // JSON 문자열 그대로 반환

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to get response from OpenAI API\"}";
        }
    }
}
