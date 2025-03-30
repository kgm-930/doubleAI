package com.rabu.doubleai.service;

import com.rabu.doubleai.model.User;
import com.rabu.doubleai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 처리
    public String registerUser(User user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(user);
        return "회원가입이 완료되었습니다";
    }

    // 로그인 처리
    public String loginUser(User user) {
        return userRepository.findByUsername(user.getUsername())
                .filter(existingUser -> passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))
                .map(u -> "로그인 성공")
                .orElse("아이디나 패스워드가 틀렸습니다");
    }
}
