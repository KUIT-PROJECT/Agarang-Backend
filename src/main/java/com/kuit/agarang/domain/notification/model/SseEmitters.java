package com.kuit.agarang.domain.notification.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SseEmitters {
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public SseEmitter add(SseEmitter emitter) {
    this.emitters.add(emitter);
    log.info("new emitter added: {}", emitter);
    log.info("emitter list size: {}", emitters.size());

    emitter.onCompletion(() -> {
      log.info("onCompletion callback");
      this.emitters.remove(emitter);
    });

    emitter.onTimeout(() -> {
      log.info("onTimeout callback");
      emitter.complete();
    });

    return emitter;
  }

  public void sendNotification(String message) {
    emitters.forEach(emitter -> {
      try {
        emitter.send(SseEmitter.event()
            .name("notification")
            .data(message));
        log.info("Notification sent: {}", message);
      } catch (IOException e) {
        log.error("Error sending notification: {}", e.getMessage());
        emitter.complete();
      }
    });
  }
}
