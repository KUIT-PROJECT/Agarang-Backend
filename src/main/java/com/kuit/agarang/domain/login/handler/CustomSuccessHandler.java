package com.kuit.agarang.domain.login.handler;

import com.kuit.agarang.domain.login.service.JWTService;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.login.utils.CookieUtil;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final AuthenticationUtil authenticationUtil;
  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;
  private final JWTService jwtService;
//  @Value("${app.baseUrl}")
//  private String baseUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

    // 유저 정보
    String providerId = authenticationUtil.getProviderId();
    String role = authenticationUtil.getRole();

    // 토큰 생성
    String access = jwtUtil.createAccessToken(providerId, role);
    String refresh = jwtUtil.createRefreshToken(providerId, role);

    // Refresh 토큰 저장
    jwtService.addRefreshEntity(providerId, refresh);

    // 응답 설정
    response.setHeader("Authorization", "Bearer " + access);
    response.addCookie(cookieUtil.createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());
    response.sendRedirect("http://localhost:8080"); // 성공 시 redirect url
  }
}
