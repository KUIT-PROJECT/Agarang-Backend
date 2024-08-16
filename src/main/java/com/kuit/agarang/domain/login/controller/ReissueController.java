package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.model.dto.ReissueDto;
import com.kuit.agarang.domain.login.service.JWTService;
import com.kuit.agarang.domain.login.utils.CookieUtil;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JWTService jwtService;
  private final CookieUtil cookieUtil;

  @PostMapping("/reissue")
  public ResponseEntity<BaseResponse<ReissueDto>> reissue(@CookieValue(value = "REFRESH", required = false) String refresh,
                                                          HttpServletResponse response) {
    ReissueDto reissueDto = jwtService.reissueTokens(refresh);

    response.addHeader("Authorization", reissueDto.getNewAccessToken());
    response.addCookie(cookieUtil.createCookie("REFRESH", reissueDto.getNewRefreshToken()));
    log.info("New Access Token = {}", reissueDto.getNewAccessToken());
    log.info("New Refresh Token = {}", reissueDto.getNewRefreshToken());

    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }
}