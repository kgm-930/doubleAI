package com.rabu.doubleai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {

    @Value("${openai.key}")
    private String API_KEY;  // OpenAI API Key

    String API_URL = "https://api.openai.com/v1/chat/completions";// OpenAI Chat API 엔드포인트

    public String getChatGptResponse(String userInput) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정
        String requestBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"Hello!\"}\n" +
                "  ]\n" +
                "}";

        // HTTP 엔티티 생성
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Response: " + response.getBody());

        // 응답 본문 반환
        return response.getBody();
    }
}
