package com.kuit.agarang.domain.login.handler;

import com.kuit.agarang.domain.login.util.AuthenticationUtil;
import com.kuit.agarang.domain.login.util.CookieUtil;
import com.kuit.agarang.domain.login.util.JWTUtil;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import com.kuit.agarang.domain.member.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

    // 유저 정보
    String providerId = AuthenticationUtil.getProviderId();
    String role = AuthenticationUtil.getRole();

    // 토큰 생성
    String access = jwtUtil.createAccessToken(providerId, role);
    String refresh = jwtUtil.createRefreshToken(providerId, role);

    // Refresh 토큰 저장
    addRefreshEntity(refresh);

    // 응답 설정 -> 성공 시 RedirctUrl
    successRedirect(response, access, refresh,"http://localhost:8080/");
  }

  private static void successRedirect(HttpServletResponse response, String access, String refresh, String redirectUrl) throws IOException {
    response.setHeader("access", access);
    response.addCookie(CookieUtil.createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());
    response.sendRedirect(redirectUrl); // 성공 시 redirect url
  }

  private void addRefreshEntity(String token) {

    RefreshToken refreshToken = RefreshToken.builder()
        .providerId(jwtUtil.getProviderId(token))
        .value(token)
        .expiration(jwtUtil.getExpiration(token).toString()).build();

    refreshRepository.save(refreshToken);
  }
}