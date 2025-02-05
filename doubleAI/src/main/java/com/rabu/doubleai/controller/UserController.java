package com.rabu.doubleai.controller;

import com.rabu.doubleai.model.User;
import com.rabu.doubleai.service.UserService;
import com.rabu.doubleai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 로그인 (로그인 성공 시 JWT 반환)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String loginResponse = userService.loginUser(user); // 로그인 처리
        if ("로그인 성공".equals(loginResponse)) { // 로그인 성공 시 JWT 생성
            String token = jwtUtil.generateToken(user.getUsername()); // JWT 생성
            return ResponseEntity.ok(token); // HTTP 200 OK, JWT 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse); // 로그인 실패 시 401 Unauthorized, 실패 메시지 반환
        }
    }
}
