package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.MessageRequest;
import com.kuit.agarang.domain.ai.model.dto.TypecastAudioResponse;
import com.kuit.agarang.domain.ai.model.dto.TypecastRequest;
import com.kuit.agarang.domain.ai.model.dto.TypecastResponse;
import com.kuit.agarang.domain.ai.utils.TypecastClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypecastService {

  @Value("${typecast.actorId}")
  private String actorId;
  private final TypecastClientUtil typeCastClientUtil;

  public String getAudioDownloadUrl(MessageRequest request) throws InterruptedException {
    TypecastResponse typecastResponse =
      typeCastClientUtil.post("/api/speak", TypecastRequest.create(request, actorId), TypecastResponse.class);

    Thread.sleep(1500); // TODO : Tread 삭제 후 call-back url 로 구현하기

    TypecastAudioResponse typecastAudioResponse =
      typeCastClientUtil.get(typecastResponse.getResult().getSpeakV2Url(), TypecastAudioResponse.class);
    return typecastAudioResponse.getResult().getAudioDownloadUrl();
  }
}
