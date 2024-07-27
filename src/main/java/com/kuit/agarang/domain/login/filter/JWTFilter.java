package com.kuit.agarang.domain.login.filter;

import com.kuit.agarang.domain.login.util.JWTUtil;
import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String accessToken = request.getHeader("access");

    // 토큰이 없다면 다음 필터로 넘김
    if (accessToken == null) {

      filterChain.doFilter(request, response);

      return;
    }

    // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {

      PrintWriter writer = response.getWriter();
      writer.print("access token expired");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 토큰 만료 시 상태 코드
      return;
    }

    // 토큰이 access인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(accessToken);

    if (!category.equals("access")) {

      PrintWriter writer = response.getWriter();
      writer.print("invalid access token");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // providerId, role 값을 획득
    String providerId = jwtUtil.getProviderId(accessToken);
    String role = jwtUtil.getRole(accessToken);


    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setProviderId(providerId);
    memberDTO.setRole(role);
    CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO);

    //스프링 시큐리티 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    //세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}