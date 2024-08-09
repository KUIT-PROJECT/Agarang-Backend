package com.kuit.agarang.domain.login.handler;

import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.login.utils.CookieUtil;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.member.repository.RefreshRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_MEMBER;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final AuthenticationUtil authenticationUtil;
  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;
  private final MemberRepository memberRepository;
  private final RefreshRepository refreshRepository;

  @Override
  @Transactional
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

    String providerId = authenticationUtil.getProviderId();
    String role = authenticationUtil.getRole();
    Long memberId = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER)).getId();

    // 토큰 생성
    String access = jwtUtil.createAccessToken(providerId, role, memberId);
    String refresh = jwtUtil.createRefreshToken(providerId, role, memberId);
    log.info("AccessToken = {}", access);

    // Refresh 토큰 저장
    RefreshToken refreshToken = RefreshToken.of(refresh);
    refreshRepository.save(refreshToken);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));
    member.addRefreshToken(refreshToken);
    memberRepository.save(member);

    // 응답 설정
    response.addCookie(cookieUtil.createCookie("Authorization", access));
    response.setStatus(HttpStatus.OK.value());
  }
}