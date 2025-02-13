package com.rabu.doubleai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {

    @Value("${openai.key}")
    private String API_KEY;  // OpenAI API Key

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatGptResponse(String userInput) {
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY.substring(1,API_KEY.length()-2));
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "  \"messages\": [\n" +
                        "    {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                        "    {\"role\": \"user\", \"content\": \"%s\"}\n" +
                        "  ]\n" +
                        "}", userInput);

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 호출
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
