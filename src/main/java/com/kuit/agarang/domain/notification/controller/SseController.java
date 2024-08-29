package com.kuit.agarang.domain.notification.controller;

import com.kuit.agarang.domain.notification.model.SseEmitters;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SseController {

  private final SseEmitters sseEmitters;

  @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect() {
    SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
    sseEmitters.add(emitter);
    try {
      emitter.send(SseEmitter.event()
          .name("connect")
          .data("connected!")); // 503에러 방지를 위한 더미데이터
    } catch (IOException e) {
      emitter.completeWithError(e);
      throw new BusinessException(BaseResponseStatus.FAIL_CREATE_EMITTER);
    }
    return emitter;
  }
}
