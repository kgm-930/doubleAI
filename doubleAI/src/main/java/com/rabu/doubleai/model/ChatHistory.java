package com.rabu.doubleai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String question;
    private String answer;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ChatHistory() {}

    public ChatHistory(User user, String question, String answer) {
        this.user = user;
        this.question = question;
        this.answer = answer;
    }
}
