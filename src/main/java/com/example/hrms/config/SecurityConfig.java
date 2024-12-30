package com.example.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // 메서드 보안 활성화
public class SecurityConfig {

    // PasswordEncoder 빈 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain 빈 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 정적 리소스와 회원가입, 비밀번호 재설정 경로 허용
                .requestMatchers("/register", "/register/**", "/forgot-password", "/forgot-password/**", "/css/**", "/js/**", "/images/**").permitAll()
                // ADMIN 전용 경로
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // /members 및 /address-book 접근을 ADMIN과 USER 역할로 허용
                .requestMatchers("/members/**", "/address-book/**").hasAnyRole("ADMIN", "USER")
                // USER 전용 경로
                .requestMatchers("/user/**").hasRole("USER")
                // 대시보드 경로 인증 필요
                .requestMatchers("/dashboard/**", "/dashboard").authenticated()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/dashboard", true) // 로그인 성공 시 리디렉션 경로
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf().disable(); // 필요에 따라 CSRF 보호 활성화/비활성화
        return http.build();
    }
}