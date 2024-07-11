package com.kuit.agarang.domain.memory.service;

import com.kuit.agarang.domain.memory.dto.MessageRequest;
import com.kuit.agarang.domain.memory.dto.TypecastAudioResponse;
import com.kuit.agarang.domain.memory.dto.TypecastRequest;
import com.kuit.agarang.domain.memory.dto.TypecastResponse;
import com.kuit.agarang.domain.memory.utils.TypecastClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypecastService {

  private final TypecastClientUtil typeCastClientUtil;

  public String getAudioDownloadUrl(MessageRequest request) throws InterruptedException {
    TypecastResponse typecastResponse =
      typeCastClientUtil.post("/api/speak", TypecastRequest.create(request), TypecastResponse.class);

    Thread.sleep(1500); // TODO : Tread 삭제 후 call-back url 로 구현하기

    TypecastAudioResponse typecastAudioResponse =
      typeCastClientUtil.get(typecastResponse.getResult().getSpeakV2Url(), TypecastAudioResponse.class);
    return typecastAudioResponse.getResult().getAudioDownloadUrl();
  }
}