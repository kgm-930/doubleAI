package com.rabu.doubleai.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping("/ask")
    public String askQuestion(@RequestBody String question){

        return "당신은 " + question;
    }
}
