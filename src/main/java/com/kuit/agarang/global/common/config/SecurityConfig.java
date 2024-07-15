package com.kuit.agarang.global.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
      .cors((cors) -> cors.disable()) // 모든 도메인 일시적 허용
      .csrf((auth) -> auth.disable()) // csrf 일시적 비활성화
      .formLogin((auth) -> auth.disable()) // 기본 로그인 폼 비활성화
      .httpBasic((auth) -> auth.disable()); // HTTP 헤더 인증 방식 비활성화

    http
      .authorizeHttpRequests((auth) -> auth
        .requestMatchers(
          "/hc", "/env")
        .permitAll()
        .anyRequest().permitAll());

    return http.build();
  }
}