package com.kuit.agarang.domain.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTChat;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTImageDescription;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTQuestionResponse;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTQuestionResult;
import com.kuit.agarang.domain.ai.model.entity.GPTChatHistory;
import com.kuit.agarang.domain.ai.repository.GPTChatHistoryRepository;
import com.kuit.agarang.domain.ai.repository.TypecastAudioRepository;
import com.kuit.agarang.domain.ai.utils.GPTRequestUtil;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.utils.S3FileUtil;
import com.kuit.agarang.global.s3.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryAIService {

  private final GPTService gptService;
  private final TypecastService typecastService;
  private final GPTRequestUtil gptRequestUtil;
  private final S3Util s3Util;
  private final S3FileUtil s3FileUtil;

  private final GPTChatHistoryRepository gptChatHistoryRepository;
  private final TypecastAudioRepository typecastAudioRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public GPTQuestionResponse getFirstQuestion(MultipartFile image) throws Exception {
    S3File s3File = s3FileUtil.uploadTempFile(image);
    String convertedGPTImageUrl = gptRequestUtil.convert(s3File);

    // image -> gpt -> 노래제목, 해시태그 생성
    GPTChat imageChat = gptService.getImageDescription(convertedGPTImageUrl);
    GPTImageDescription imageDescription = GPTImageDescription.from(imageChat.getResponseMessage().getContent());

    // 해시태그 -> gpt ->  질문1 생성
    GPTChat questionChat = gptService.createFirstQuestion(imageDescription);
    String question = (String) questionChat.getResponseMessage().getContent();

    // 질문1 -> tts -> 오디오 변환
    String typecastAudioId = typecastService.getAudioDownloadUrl(question);
    String questionAudioUrl = null;
    if (checkEntityExistence(typecastAudioId)) {
      questionAudioUrl = typecastAudioRepository.findById(typecastAudioId).get().getAudioDownloadUrl();
    }

    // 대화기록 저장 및 임시저장이 필요한 데이터 저장
    // TODO : redis cache 전환
    GPTChatHistory history = gptChatHistoryRepository.save(GPTChatHistory.builder()
      .id(questionChat.getGptResponse().getId())
      .imageTempPath(s3File.getContentType().getPath() + s3File.getFilename())
      .musicTitle(imageDescription.getText())
      .hashtags(imageDescription.getNoun().toString())
      .historyMessages(objectMapper.writeValueAsString(gptRequestUtil.addHistoryMessage(questionChat)))
      .questionAudioUrl(questionAudioUrl)
      .build());

    return new GPTQuestionResponse(GPTQuestionResult.builder()
      .id(history.getId())
      .text(questionChat.getResponseMessage().getContent().toString())
      .audioUrl(history.getQuestionAudioUrl())
      .build());
  }

  // TODO : redis 트리거 전환
  public boolean checkEntityExistence(String entityId) {
    try {
      log.info("1차 대기");
      Thread.sleep(1500);

      if (typecastAudioRepository.existsById(entityId)) {
        return true;
      } else {
        log.info("2차 대기");
        Thread.sleep(1500);
        return typecastAudioRepository.existsById(entityId);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt(); // 인터럽트 상태를 복구
      return false;
    }
  }
}
