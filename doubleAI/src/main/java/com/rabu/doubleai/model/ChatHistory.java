package com.rabu.doubleai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 여러 개의 채팅 기록이 하나의 사용자와 연결
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String question;
    private String answer;
    private LocalDateTime timestamp;

    public ChatHistory() {
        this.timestamp = LocalDateTime.now(); // 자동으로 저장 시간 기록
    }

    public ChatHistory(User user, String question, String answer) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
