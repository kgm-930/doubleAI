package com.rabu.doubleai.security;

import com.rabu.doubleai.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component  // Spring Bean으로 등록
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 생성자 주입
    public JwtTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null && jwtUtil.validateToken(token)) {
            // 토큰이 유효하면, 해당 토큰에서 유저 정보를 추출하여 SecurityContext에 설정
            String username = jwtUtil.extractUsername(token);
            // 유저 인증 설정 (UsernamePasswordAuthenticationToken 사용)
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));
        }

        filterChain.doFilter(request, response); // 다음 필터로 진행
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);  // "Bearer " 부분을 제거한 토큰 반환
        }
        return null;
    }
}
