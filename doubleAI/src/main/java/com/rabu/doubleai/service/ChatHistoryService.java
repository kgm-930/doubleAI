package com.rabu.doubleai.service;

import com.rabu.doubleai.model.ChatHistory;
import com.rabu.doubleai.model.User;
import com.rabu.doubleai.repository.ChatHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public void saveChatHistory(User user, String question, String answer) {
        ChatHistory chatHistory = new ChatHistory(user, question, answer);
        chatHistoryRepository.save(chatHistory);
    }

    public List<ChatHistory> getChatHistory(User user) {
        return chatHistoryRepository.findByUserOrderByTimestampDesc(user);
    }
}
