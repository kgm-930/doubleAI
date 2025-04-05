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

    public void saveChat(User user, String question, String answer) {
        ChatHistory history = new ChatHistory(user, question, answer);
        chatHistoryRepository.save(history);
    }

    public List<ChatHistory> getHistory(User user) {
        return chatHistoryRepository.findByUserOrderByTimestampAsc(user);
    }
}
