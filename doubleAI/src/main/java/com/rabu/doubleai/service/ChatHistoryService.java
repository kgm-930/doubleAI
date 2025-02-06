package com.rabu.doubleai.service;

import com.rabu.doubleai.model.ChatHistory;
import com.rabu.doubleai.repository.ChatHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    // 대화 기록 저장
    public void saveChatHistory(String username, String question, String answer) {
        ChatHistory chatHistory = ChatHistory.builder()
                .username(username)
                .question(question)
                .answer(answer)
                .build();

        chatHistoryRepository.save(chatHistory);
    }
}
