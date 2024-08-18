package com.kuit.agarang.domain.login.filter;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import com.kuit.agarang.global.common.exception.exception.JWTException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public final static List<String> PASS_URIS = Arrays.asList(
    "/reissue",
    "/login",
    "/api/login/success"
  );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    if (isPassUris(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("ACCESS".equals(cookie.getName())) {
          accessToken = cookie.getValue();
          break;
        }
      }
    }

    if (accessToken == null) {
      SecurityContextHolder.getContext().setAuthentication(null);
      filterChain.doFilter(request, response);
      return;
    }

    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      throw new JWTException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN);
    } catch (Exception e) {
      throw new JWTException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
    }

    if (!"access".equals(jwtUtil.getCategory(accessToken))) {
      throw new JWTException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
    }

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(MemberDTO.builder()
      .memberId(jwtUtil.getMemberId(accessToken))
      .providerId(jwtUtil.getProviderId(accessToken))
      .role(jwtUtil.getRole(accessToken)).build());

    //스프링 시큐리티 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

    //세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    log.info("JWT Filter Success");
    filterChain.doFilter(request, response);
  }

  private boolean isPassUris(String uri) {
    return PASS_URIS.contains(uri);
  }
}