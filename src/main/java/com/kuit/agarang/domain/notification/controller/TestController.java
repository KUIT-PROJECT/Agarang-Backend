package com.kuit.agarang.domain.notification.controller;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.notification.service.SseService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

  private final SseService sseService;

  @GetMapping
  public ResponseEntity<BaseResponse<Void>> test(@AuthenticationPrincipal CustomOAuth2User details) {
    sseService.sendNotification(details.getMemberId(), "테스트 알림 입니다.");
    return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
  }
}
