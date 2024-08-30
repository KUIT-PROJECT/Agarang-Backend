package com.kuit.agarang.domain.notification.service;


import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

  private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter connect(Long memberId) {

    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    try {
      emitter.send(SseEmitter.event()
          .name("connect")
          .data("connected!")); // 503에러 방지를 위한 더미
    } catch (IOException e) {
      emitter.completeWithError(e);
      throw new BusinessException(BaseResponseStatus.FAIL_CREATE_EMITTER);
    }

    emitter.onError(e -> {
      log.error("Error on SSE connection for memberId: {}", memberId, e);
      emitters.remove(memberId);
    });

    emitter.onCompletion(() -> {
      log.info("onCompletion callback for memberId: {}", memberId);
      emitters.remove(memberId);
    });

    emitter.onTimeout(() -> {
      log.info("onTimeout callback for memberId: {}", memberId);
      emitter.complete();
    });

    emitters.put(memberId, emitter);
    return emitter;
  }

  public void sendOneNotification(Long memberId, String message) {
    SseEmitter emitter = emitters.get(memberId);
    log.info("memberId : {} , sendNotification", memberId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name("notification")
            .data(message, MediaType.TEXT_PLAIN));
      } catch (IOException e) {
        emitters.remove(memberId);
      } finally {
        emitters.remove(memberId);
      }
    }
  }

  public void disconnect(Long memberId) {
    log.info("memberId : {} , disconnect", memberId);
    emitters.remove(memberId);
  }
}
