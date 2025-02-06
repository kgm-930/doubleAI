package com.rabu.doubleai.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequest {
    private String question;

    // 기본 생성자, 게터, 세터
    public ChatRequest() {}

    public ChatRequest(String question) {
        this.question = question;
    }

}
