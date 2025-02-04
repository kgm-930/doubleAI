package com.rabu.doubleai.controller;

import com.rabu.doubleai.model.User;
import com.rabu.doubleai.service.UserService;
import com.rabu.doubleai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody User user){
        return userService.registerUser(user);
    }

    // 로그인 (로그인 성공 시 JWT 반환)
    @PostMapping("/login")
    public String login(@RequestBody User user){
        String loginResponse = userService.loginUser(user);
        if (loginResponse.equals("로그인 성공")) {
            return jwtUtil.generateToken(user.getUsername()); // JWT 반환
        } else {
            return loginResponse; // 로그인 실패 메시지 반환
        }
    }
}
