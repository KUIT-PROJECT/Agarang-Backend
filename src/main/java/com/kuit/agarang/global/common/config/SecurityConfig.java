package com.kuit.agarang.global.common.config;

import com.kuit.agarang.domain.login.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final DefaultOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

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
                "/hc", "/env", "/oauth2/**")
            .permitAll()
            .anyRequest().permitAll()
        )
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
            .successHandler(oAuth2SuccessHandler)
        );

    return http.build();
  }
}