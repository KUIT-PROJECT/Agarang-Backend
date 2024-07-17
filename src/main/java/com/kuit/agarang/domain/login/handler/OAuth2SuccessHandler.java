package com.kuit.agarang.domain.login.handler;


import com.kuit.agarang.domain.login.model.entitiy.CustomOAuth2User;
import com.kuit.agarang.domain.login.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  @Override

  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException, ServletException {

    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

    String userId = oAuth2User.getName();
    String token = jwtProvider.createToken(userId);

    response.sendRedirect("http://localhost:8080/auth/oauth-response/" + token + "/3600"); // 3600초

  }
}
