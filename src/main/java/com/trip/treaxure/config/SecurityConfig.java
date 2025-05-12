package com.trip.treaxure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (H2 콘솔 등 접근 위해)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", 
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated() // 나머지 경로는 인증 필요
            );
//            .formLogin(form -> form.loginPage("/")
//                .permitAll() // 별도 loginPage 없이 기본 로그인 페이지 사용
//            )
//            .logout(logout -> logout
//                .logoutSuccessUrl("/") // 로그아웃 후 리디렉션
//                .permitAll()
//            );

        // H2 콘솔 프레임 허용 (frameOptions 비활성화)
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
