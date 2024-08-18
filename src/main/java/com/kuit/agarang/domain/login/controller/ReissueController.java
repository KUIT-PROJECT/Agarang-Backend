package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.model.dto.ReissueDto;
import com.kuit.agarang.domain.login.service.JWTService;
import com.kuit.agarang.domain.login.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JWTService jwtService;
  private final CookieUtil cookieUtil;

  @Value("${app.baseUrl}")
  private String baseUrl;
  private static final String LOGIN_SUCCESS_URI = "/api/login/success";

  @PostMapping("/reissue")
  public void reissue(@CookieValue(value = "REFRESH", required = false) String refresh,
                                                          HttpServletResponse response) throws IOException {
    ReissueDto reissueDto = jwtService.reissueTokens(refresh);

    response.addCookie(cookieUtil.createCookie("ACCESS", reissueDto.getNewAccessToken()));
    response.addCookie(cookieUtil.createCookie("REFRESH", reissueDto.getNewRefreshToken()));
    response.sendRedirect(baseUrl + LOGIN_SUCCESS_URI);
  }
}