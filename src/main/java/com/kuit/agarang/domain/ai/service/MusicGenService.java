package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.musicGen.MusicGenRequest;
import com.kuit.agarang.domain.ai.model.dto.musicGen.MusicGenResponse;
import com.kuit.agarang.domain.ai.utils.MusicGenClientUtil;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.domain.notification.service.SseService;
import com.kuit.agarang.global.common.exception.exception.OpenAPIException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicGenService {

  @Value("${app.baseUrl}")
  private String baseUrl;
  @Value("${music-gen.version}")
  private String version;
  private final MusicGenClientUtil musicGenClientUtil;
  private final S3Util s3Util;
  private final MemoryRepository memoryRepository;
  private final SseService sseService;

  private static final String WEBHOOK_URI = "/api/ai/music-gen/webhook";
  private static final Integer MUSIC_DURATION = 40;

  public String getMusic(String prompt) {
    MusicGenResponse response
        = musicGenClientUtil.post(
        new MusicGenRequest(version, baseUrl + WEBHOOK_URI, prompt, MUSIC_DURATION), MusicGenResponse.class);
    log.info("created musicgen id : {}", response.getId());
    return response.getId();
  }

  public void saveMusic(MusicGenResponse response) {
    checkStatus(response);

    Optional<Memory> optionalMemory = memoryRepository.findByMusicGenId(response.getId());
    if (optionalMemory.isEmpty()) {
      log.error("music gen webhook error : not found member by musicgenId : {}", response.getId());
      return;
    }

    S3File s3File = s3Util.downloadAudioAndUpload(response.getOutput());
    Memory memory = optionalMemory.get();
    memory.setMusicUrl(s3File.getObjectUrl());
    memoryRepository.save(memory);

    String message = "음악 생성 완료!";
    sseService.sendOneNotification(memory.getMember().getId(), message);
  }

  private static void checkStatus(MusicGenResponse response) {
    log.info("webhook musicgen id : {}", response.getId());
    if ("failed".equals(response.getStatus())) {
      log.error("musicgen error {}", response.getError());
      throw new OpenAPIException(BaseResponseStatus.FAIL_CREATE_MUSIC);
    }

    log.info("webhook musicgen started at : {}", response.getStartedAt());
    log.info("webhook musicgen completed at : {}", response.getCompletedAt());
  }
}
