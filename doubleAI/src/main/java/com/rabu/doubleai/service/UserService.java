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
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "로그인 성공"; // JWT 발급 로직은 컨트롤러에서 처리하거나 별도의 메서드로 분리
        } else {
            return "아이디나 패스워드가 틀렸습니다";
        }
    }
}
