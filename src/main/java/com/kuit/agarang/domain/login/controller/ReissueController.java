package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.util.JWTUtil;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import com.kuit.agarang.domain.member.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;
  @Value("${secret.jwt-access-expired-in}")
  private Long ACCESS_EXPIRED_IN;
  @Value("${secret.jwt-refresh-expired-in}")
  private Long REFRESH_EXPIRED_IN;

  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    if (refresh == null) {
      return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
    }

    // 만료 여부 체크
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
    }

    // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);

    if (!category.equals("refresh")) {
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    //DB에 저장되어 있는지 확인
    Boolean isExist = refreshRepository.existsByValue(refresh);
    if (!isExist) {
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }


    String providerId = jwtUtil.getProviderId(refresh);
    String role = jwtUtil.getRole(refresh);

    // AccessToken 생성
    String newAccess = jwtUtil.createAccessToken(providerId, role);
    // refresh rotate
    String newRefresh = jwtUtil.createRefreshToken(providerId, role);

    //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
    refreshRepository.deleteByValue(refresh);
    addRefreshEntity(providerId, newRefresh, REFRESH_EXPIRED_IN);

    response.setHeader("access", newAccess);
    response.addCookie(createCookie("refresh", newRefresh));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    //cookie.setSecure(true);
    //cookie.setPath("/");
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