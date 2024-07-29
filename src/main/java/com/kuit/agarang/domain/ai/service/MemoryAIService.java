package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.gpt.*;
import com.kuit.agarang.domain.ai.model.entity.cache.GPTChatHistory;
import com.kuit.agarang.domain.ai.utils.GPTUtil;
import com.kuit.agarang.global.common.service.RedisService;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.utils.S3FileUtil;
import com.kuit.agarang.global.s3.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryAIService {

  private final GPTUtil gptUtil;
  private final GPTService gptService;
  private final TypecastService typecastService;

  private final S3Util s3Util;
  private final S3FileUtil s3FileUtil;

  private final RedisService redisService;

  public GPTQuestionResponse getFirstQuestion(MultipartFile image) throws Exception {
    S3File s3File = s3FileUtil.uploadTempFile(image);
    String convertedGPTImageUrl = gptUtil.convertToString(s3File);

    // image -> gpt -> 노래제목, 해시태그 생성
    GPTChat imageChat = gptService.getImageDescription(convertedGPTImageUrl);
    GPTImageDescription imageDescription = GPTImageDescription.from(gptUtil.getGPTAnswer(imageChat));

    // 해시태그 -> gpt ->  질문1 생성
    GPTChat questionChat = gptService.createFirstQuestion(imageDescription);
    String question = gptUtil.getGPTAnswer(questionChat);

    // 질문1 -> tts -> 오디오 변환
    String typecastAudioId = typecastService.getAudioDownloadUrl(question);
    String questionAudioUrl = null;
    if (checkEntityExistence(typecastAudioId)) {
      questionAudioUrl = redisService.get(typecastAudioId, String.class);
      redisService.delete(typecastAudioId);
    }

    // 대화기록 저장 및 임시저장이 필요한 데이터 저장
    String redisKey = questionChat.getGptResponse().getId();
    List<GPTMessage> historyMessage = gptUtil.getHistoryMessage(questionChat);
    redisService.save(redisKey,
      GPTChatHistory.builder()
        .imageTempPath(s3File.getFilename())
        .imageDescription(imageDescription)
        .questionAudioUrl(questionAudioUrl)
        .historyMessages(historyMessage)
        .build());

    return new GPTQuestionResponse(GPTQuestionResult.builder()
      .id(redisKey)
      .text(question)
      .audioUrl(questionAudioUrl)
      .build());
  }

  // TODO : redis 트리거 전환
  public boolean checkEntityExistence(String key) {
    try {
      log.info("1차 대기");
      Thread.sleep(1500);

      if (redisService.existsByKey(key)) {
        return true;
      } else {
        log.info("2차 대기");
        Thread.sleep(1500);
        return redisService.existsByKey(key);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt(); // 인터럽트 상태를 복구
      return false;
    }
  }
}
