package com.rabu.doubleai.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private LocalDateTime timestamp = LocalDateTime.now();

    public ChatHistory() {}

    public ChatHistory(User user, String question, String answer) {
        this.user = user;
        this.question = question;
        this.answer = answer;
    }
}
