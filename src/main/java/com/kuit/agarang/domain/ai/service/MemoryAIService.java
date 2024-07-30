package com.kuit.agarang.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.Answer;
import com.kuit.agarang.domain.ai.model.dto.QuestionResponse;
import com.kuit.agarang.domain.ai.model.dto.QuestionResult;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTChat;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTImageDescription;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import com.kuit.agarang.domain.ai.model.entity.cache.GPTChatHistory;
import com.kuit.agarang.domain.ai.utils.GPTUtil;
import com.kuit.agarang.global.common.service.RedisService;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.utils.S3FileUtil;
import com.kuit.agarang.global.s3.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.annotation.Async;
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
  private final ObjectMapper objectMapper;

  public QuestionResponse getFirstQuestion(MultipartFile image) throws Exception {
    S3File s3File = s3FileUtil.uploadTempFile(image);
    String convertedGPTImageUrl = gptUtil.convert(s3File);

    // image -> gpt -> 노래제목, 해시태그 생성
    GPTChat imageChat = gptService.getImageDescription(convertedGPTImageUrl);
    GPTImageDescription imageDescription = GPTImageDescription.from(gptUtil.getGPTAnswer(imageChat));

    // 해시태그 -> gpt ->  질문1 생성
    GPTChat questionChat = gptService.createImageQuestion(imageDescription);
    String question = gptUtil.getGPTAnswer(questionChat);

    // 질문1 -> tts -> 오디오 변환
    String questionAudioUrl = getAudioUrl(question);

    // 대화기록 저장 및 임시저장이 필요한 데이터 저장
    String redisKey = questionChat.getGptResponse().getId();
    List<GPTMessage> historyMessage = gptUtil.createHistoryMessage(questionChat);
    redisService.save(redisKey,
      GPTChatHistory.builder()
        .imageTempPath(s3File.getFilename())
        .hashtags(imageDescription.getNoun())
        .historyMessages(historyMessage)
        .build());

    logChat(historyMessage);
    return new QuestionResponse(QuestionResult.builder()
      .id(redisKey)
      .text(question)
      .audioUrl(questionAudioUrl)
      .build());
  }

  private @Nullable String getAudioUrl(String question) {
    String typecastAudioId = typecastService.getAudioDownloadUrl(question);
    String questionAudioUrl = null;
    if (checkEntityExistence(typecastAudioId)) {
      questionAudioUrl = redisService.get(typecastAudioId, String.class)
        .orElseThrow(() -> new RuntimeException(""));
      redisService.delete(typecastAudioId);
    }
    return questionAudioUrl;
  }

  public QuestionResponse getNextQuestion(Answer answer) {
    GPTChatHistory chatHistory = redisService.get(answer.getId(), GPTChatHistory.class)
      .orElseThrow(() -> new RuntimeException(""));

    GPTChat chat = gptService.chatWithHistory(chatHistory.getHistoryMessage(), answer.getText());
    String question = gptUtil.getGPTAnswer(chat);

    String questionAudioUrl = getAudioUrl(question);

    logChat(gptUtil.createHistoryMessage(chat));
    redisService.save(answer.getId(), chatHistory);

    return new QuestionResponse(QuestionResult.builder()
      .id(answer.getId())
      .text(question)
      .audioUrl(questionAudioUrl)
      .build());
  }

  public void saveLastAnswer(Answer answer) {
    GPTChatHistory chatHistory = redisService.get(answer.getId(), GPTChatHistory.class)
      .orElseThrow(() -> new RuntimeException(""));

    chatHistory.getHistoryMessage().add(gptUtil.createTextMessage(answer.getText()));
    logChat(chatHistory.getHistoryMessage());
    redisService.save(answer.getId(), chatHistory);
  }

  @Async
  public void createMemoryText(String gptChatHistoryId) {
    GPTChatHistory chatHistory = redisService.get(gptChatHistoryId, GPTChatHistory.class)
      .orElseThrow(() -> new RuntimeException(""));

    // TODO : memberId 로 필드 조회
    String prompt = gptUtil.convert("아빠", "뿌둥");
    GPTChat chat = gptService.chatWithHistory(chatHistory.getHistoryMessage(), prompt);

    logChat(gptUtil.createHistoryMessage(chat));
    redisService.save(gptChatHistoryId, chatHistory);
  }

  // TODO : redis 트리거 전환
  public boolean checkEntityExistence(String key) {
    try {
      for (int i = 1; i < 3; i++) {
        log.info(i + "차 대기");
        Thread.sleep(1500);

        if (redisService.existsByKey(key)) {
          return true;
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // 인터럽트 상태를 복구
      throw new RuntimeException(e);
    }
    return false;
  }

  private void logChat(List<GPTMessage> historyMessage) {
    try {
      log.info(objectMapper.writeValueAsString(historyMessage));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
