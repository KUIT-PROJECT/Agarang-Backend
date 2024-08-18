package com.kuit.agarang.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.login.handler.CustomSuccessHandler;
import com.kuit.agarang.domain.login.filter.JWTFilter;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import com.kuit.agarang.domain.login.service.CustomOAuth2UserService;
import com.kuit.agarang.global.common.exception.handler.FilterExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JWTUtil jwtUtil;

  @Bean
  public JWTFilter jwtFilter() {
    return new JWTFilter(jwtUtil);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .cors((cors) -> cors.disable()) // 모든 도메인 일시적 허용
        .csrf((auth) -> auth.disable()) // csrf 일시적 비활성화
        .formLogin((auth) -> auth.disable()) // 기본 로그인 폼 비활성화
        .httpBasic((auth) -> auth.disable()); // HTTP 헤더 인증 방식 비활성화

    http
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new FilterExceptionHandler(new ObjectMapper()), JWTFilter.class);

    //oauth2
    http
        .oauth2Login((oauth2) -> oauth2
            .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(customOAuth2UserService)))
            .successHandler(customSuccessHandler));

    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers(
                "/", "/env", "/api-json/**", "/api-docs", "/swagger-ui/**").permitAll()
            .requestMatchers("/oauth2/**", "/login/**","/reissue").permitAll()
            .requestMatchers("/api/ai/music-gen/webhook", "/api/ai/tts/webhook").permitAll()
            .anyRequest().permitAll()
            // .anyRequest().hasRole("USER")
        )
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
            .successHandler(customSuccessHandler)
        );

    return http.build();
  }
}
