package com.kuit.agarang.domain.login.filter;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;
  private static final String BEARER = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String accessToken = request.getHeader("Authorization");

    if (accessToken == null || "/reissue".equals(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!accessToken.startsWith(BEARER)) {
      throw new BusinessException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
    }
    accessToken = accessToken.split("Bearer ")[1];

    /*
      EXPIRED_ACCESS_TOKEN -> Reissue Controller redirect
     */
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      throw new BusinessException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN);
    }

    // 토큰이 access인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(accessToken);

    if (!category.equals("Authorization")) {
      throw new BusinessException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
    }

    String providerId = jwtUtil.getProviderId(accessToken);
    String role = jwtUtil.getRole(accessToken);
    Long memberId = jwtUtil.getMemberId(accessToken);

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(MemberDTO.builder()
        .memberId(memberId)
        .providerId(providerId)
        .role(role).build());

    //스프링 시큐리티 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

    //세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
    log.info("JWT Filter Success");
  }
}