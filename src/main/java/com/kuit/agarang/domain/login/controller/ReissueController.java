package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.login.model.dto.ReissueDto;
import com.kuit.agarang.domain.login.service.JWTService;
import com.kuit.agarang.domain.login.utils.CookieUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_MEMBER;
import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JWTService jwtService;
  private final CookieUtil cookieUtil;

  @PostMapping("/reissue")
  public ResponseEntity<BaseResponse<ReissueDto>> reissue(@AuthenticationPrincipal CustomOAuth2User details,
                                                          HttpServletRequest request, HttpServletResponse response) {

    ReissueDto reissueDto = jwtService.reissueTokens(details.getMemberId());

    response.addCookie(cookieUtil.createCookie("Authorization", reissueDto.getNewAccessToken()));
    log.info("New Access Token = {}", reissueDto.getNewAccessToken());

    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }
}