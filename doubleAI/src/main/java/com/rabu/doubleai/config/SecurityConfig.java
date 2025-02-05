package com.rabu.doubleai.config;

import com.rabu.doubleai.handler.CustomAuthenticationFailureHandler;
import com.rabu.doubleai.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 메소드 보안을 위한 어노테이션
public class SecurityConfig {

    // 특정 URL은 보안 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/user/register", "/api/user/login"); // 예외처리할 URL들
    }

    // 패스워드 인코딩
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // HTTP 보안 설정
    @Bean
    protected SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // /api/user/register, /api/user/login은 누구나 접근 가능
                .requestMatchers("/api/user/register", "/api/user/login").permitAll()
                // 그 외의 요청은 인증이 필요
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")  // 로그인 페이지 URL
                .loginProcessingUrl("/api/user/login") // 실제 로그인 처리 URL
                .permitAll() // 로그인 페이지와 처리 URL은 모두 인증 없이 접근 가능
                .successHandler(new CustomAuthenticationSuccessHandler()) // 로그인 성공 후 핸들러
                .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 후 핸들러
                .and()
                .sessionManagement()
                .invalidSessionUrl("/login") // 세션이 유효하지 않은 경우 로그인 페이지로 리디렉션
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/user/logout")) // 로그아웃 URL
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); // CSRF 설정

        return http.build();
    }
}
