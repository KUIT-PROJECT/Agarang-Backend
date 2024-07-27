package com.kuit.agarang.domain.login.handler;

import com.kuit.agarang.domain.login.jwt.JWTUtil;
import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import com.kuit.agarang.domain.member.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Value("${secret.jwt-access-expired-in}")
  private Long ACCESS_EXPIRED_IN;
  @Value("${secret.jwt-refresh-expired-in}")
  private Long REFRESH_EXPIRED_IN;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    // 유저 정보
    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
    String providerId = customUserDetails.getProviderId();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    // 토큰 생성
    String access = jwtUtil.createJwt("access", providerId, role, ACCESS_EXPIRED_IN);
    String refresh = jwtUtil.createJwt("refresh", providerId, role, REFRESH_EXPIRED_IN);

    // Refresh 토큰 저장
    addRefreshEntity(providerId, refresh, REFRESH_EXPIRED_IN);

    // 응답 설정
    response.setHeader("access", access);
    response.addCookie(createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());
    response.sendRedirect("http://localhost:8080/"); // 성공 시 redirect url
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
//    cookie.setSecure(true); https 환경에서만 쿠키 전송
//    cookie.setPath("/");
    cookie.setHttpOnly(true);

    return cookie;
  }

  private void addRefreshEntity(String providerId, String value, Long expiredMs) {

    Date date = new Date(System.currentTimeMillis() + expiredMs);

    RefreshToken refreshToken = RefreshToken.builder()
        .providerId(providerId)
        .value(value)
        .expiration(date.toString()).build();

    refreshRepository.save(refreshToken);
  }
}