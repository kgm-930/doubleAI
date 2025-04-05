package com.rabu.doubleai.repository;

import com.rabu.doubleai.model.ChatHistory;
import com.rabu.doubleai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByUserOrderByTimestampAsc(User user);
}
