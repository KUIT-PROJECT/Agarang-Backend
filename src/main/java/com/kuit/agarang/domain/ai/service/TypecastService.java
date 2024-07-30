package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.typecast.TypecastRequest;
import com.kuit.agarang.domain.ai.model.dto.typecast.TypecastResponse;
import com.kuit.agarang.domain.ai.model.dto.typecast.TypecastWebhookResponse;
import com.kuit.agarang.domain.ai.utils.TypecastClientUtil;
import com.kuit.agarang.global.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypecastService {

  @Value("${typecast.actorId}")
  private String actorId;
  private final TypecastClientUtil typeCastClientUtil;
  private final RedisService redisService;

  public String getAudioDownloadUrl(String text) {
    TypecastResponse response
      = typeCastClientUtil.post("/api/speak", TypecastRequest.create(text, actorId), TypecastResponse.class);
    return response.getResult().getSpeakUrl();
  }

  public void saveAudio(TypecastWebhookResponse response) {
    if ("failed".equals(response.getStatus())) {
      // TODO : 예외처리 (done or failed)
    }
    redisService.save(response.getSpeakUrl(), response.getAudioDownloadUrl());
  }
}
