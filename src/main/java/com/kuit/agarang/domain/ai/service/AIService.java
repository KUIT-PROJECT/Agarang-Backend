package com.kuit.agarang.domain.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.*;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTChat;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTImageDescription;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import com.kuit.agarang.domain.ai.model.entity.cache.GPTChatHistory;
import com.kuit.agarang.domain.ai.model.enums.GPTSystemRole;
import com.kuit.agarang.domain.ai.utils.GPTPromptUtil;
import com.kuit.agarang.domain.ai.utils.GPTUtil;
import com.kuit.agarang.domain.baby.model.entity.Character;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.memory.model.entity.Hashtag;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.repository.HashTagRepository;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.exception.exception.OpenAPIException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.common.service.RedisService;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.utils.S3FileUtil;
import com.kuit.agarang.global.s3.utils.S3Util;
import jakarta.transaction.Transactional;
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
public class AIService {

  private final GPTUtil gptUtil;
  private final GPTPromptUtil promptUtil;
  private final GPTChatService gptChatService;
  private final TypecastService typecastService;

  private final S3Util s3Util;
  private final S3FileUtil s3FileUtil;

  private final RedisService redisService;
  private final ObjectMapper objectMapper;

  private final MemoryRepository memoryRepository;
  private final MemberRepository memberRepository;
  private final MusicGenService musicGenService;
  private final HashTagRepository hashTagRepository;

  public QuestionResponse getFirstQuestion(MultipartFile image) throws Exception {
    S3File convertedImage = s3FileUtil.convert(image);

    // image -> gpt -> 노래제목, 해시태그 생성
    String prompt = promptUtil.createImageDescriptionPrompt();
    GPTChat imageChat = gptChatService.chatWithImage(convertedImage, prompt);
    GPTImageDescription imageDescription = gptUtil.parseJson(imageChat, GPTImageDescription.class);

    // 해시태그 -> gpt ->  질문1 생성
    prompt = promptUtil.createImageQuestionPrompt(imageDescription);
    GPTChat questionChat = gptChatService.chat(GPTSystemRole.COUNSELOR, prompt, 0L, false);
    String question = gptUtil.getGPTAnswer(questionChat);

    // 질문1 -> tts -> 오디오 변환
    String questionAudioUrl = getAudioUrl(question);

    // 대화기록 저장 및 임시저장이 필요한 데이터 저장
    String redisKey = questionChat.getGptResponse().getId();
    List<GPTMessage> historyMessage = gptUtil.createHistoryMessage(questionChat);
    redisService.save(redisKey,
      GPTChatHistory.builder()
        .image(convertedImage)
        .imageDescription(imageDescription)
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
        .orElseThrow(() -> new OpenAPIException(BaseResponseStatus.NOT_FOUND_TSS_AUDIO));
      redisService.delete(typecastAudioId);
    }
    return questionAudioUrl;
  }

  public QuestionResponse getNextQuestion(TextAnswer answer) {
    GPTChatHistory chatHistory = redisService.get(answer.getId(), GPTChatHistory.class)
      .orElseThrow(() -> new OpenAPIException(BaseResponseStatus.NOT_FOUND_HISTORY_CHAT));

    GPTChat chat = gptChatService.chatWithHistory(chatHistory.getHistoryMessages(), answer.getText(), 0L);
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

  public void saveLastAnswer(TextAnswer answer) {
    GPTChatHistory chatHistory = redisService.get(answer.getId(), GPTChatHistory.class)
      .orElseThrow(() -> new OpenAPIException(BaseResponseStatus.NOT_FOUND_HISTORY_CHAT));

    chatHistory.getHistoryMessages().add(gptUtil.createTextMessage(answer.getText()));
    logChat(chatHistory.getHistoryMessages());
    redisService.save(answer.getId(), chatHistory);
  }

  @Async
  public void createMemoryText(Long memberId, String gptChatHistoryId) {
    GPTChatHistory chatHistory = redisService.get(gptChatHistoryId, GPTChatHistory.class)
      .orElseThrow(() -> new OpenAPIException(BaseResponseStatus.NOT_FOUND_HISTORY_CHAT));

    MemoryTextInfo memoryTextInfo = getMemoryTextInfo(memberId);

    String prompt = promptUtil.createMemoryTextPrompt(memoryTextInfo);
    GPTChat chat = gptChatService.chatWithHistory(chatHistory.getHistoryMessages(), prompt, 0L);

    chatHistory.setMemoryText(gptUtil.getGPTAnswer(chat));
    logChat(gptUtil.createHistoryMessage(chat));
    redisService.save(gptChatHistoryId, chatHistory);
  }

  @Transactional
  public MemoryTextInfo getMemoryTextInfo(Long memberId) {
    Member member = memberRepository.findByIdWithBaby(memberId)
      .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));
    return MemoryTextInfo.builder()
      .familyRole(member.getFamilyRole())
      .babyName(member.getBaby().getName())
      .build();
  }

  public GPTChatHistory setMusicChoice(MusicAnswer answer) {
    GPTChatHistory chatHistory = redisService.get(answer.getId(), GPTChatHistory.class)
      .orElseThrow(() -> new OpenAPIException(BaseResponseStatus.NOT_FOUND_HISTORY_CHAT));

    chatHistory.setMusicInfo(MusicInfo.from(answer.getMusicChoice()));
    return chatHistory;
  }

  @Async
  @Transactional
  public void createMusicGenPrompt(Long memberId, GPTChatHistory chatHistory) {
    String prompt = promptUtil.createMusicGenPrompt(chatHistory.getImageDescription(), chatHistory.getMusicInfo());
    GPTChat chat = gptChatService.chat(GPTSystemRole.MUSIC_PROMPT_ENGINEER, prompt, 1L, true);
    String musicGenPrompt = gptUtil.parseJson(chat, "prompt");
    logChat(gptUtil.createHistoryMessage(chat));
    log.info("musicGenPrompt {}", musicGenPrompt);

    prompt = promptUtil.createMusicTitlePrompt(musicGenPrompt, chatHistory.getMusicInfo());
    chat = gptChatService.chat(GPTSystemRole.MUSIC_TITLE_WRITER, prompt, 1L, true);
    String musicTitle = gptUtil.parseJson(chat, "music_name");
    logChat(gptUtil.createHistoryMessage(chat));
    log.info(musicTitle);

    String musicGenId = musicGenService.getMusic(musicGenPrompt);
    S3File image = s3Util.upload(chatHistory.getImage());

    Member member = getMember(memberId);
    Memory memory = Memory.builder()
      .member(member)
      .baby(member.getBaby())
      .imageUrl(image.getObjectUrl())
      .musicTitle(musicTitle)
      .musicGenId(musicGenId)
      .text(chatHistory.getMemoryText())
      .genre(chatHistory.getMusicInfo().getGenre())
      .mood(chatHistory.getMusicInfo().getMood())
      .tempo(chatHistory.getMusicInfo().getTempo())
      .instrument(chatHistory.getMusicInfo().getInstrument())
      .build();

    List<Hashtag> hashtags = chatHistory.getImageDescription().convertNoun(memory);
    memory.setHashtags(hashtags);
    memoryRepository.save(memory);

    // TODO : playlist 구분 저장 구현 시 반영
  }

  public String getCharacterBubble(Character character, String familyRole) {
    String prompt = promptUtil.createCharacterBubble(character, familyRole);
    GPTChat chat = gptChatService.chat(GPTSystemRole.ASSISTANT, prompt, 1L, false);
    logChat(gptUtil.createHistoryMessage(chat));
    return gptUtil.getGPTAnswer(chat);
  }

  private Member getMember(Long memberId) {
    return memberRepository.findById(memberId)
      .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));
  }

  // TODO : redis 트리거 전환
  public boolean checkEntityExistence(String key) {
    try {
      for (int i = 1; i < 3; i++) {
        log.info("{}차 대기", i);
        Thread.sleep(1500);

        if (redisService.existsByKey(key)) {
          return true;
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // 인터럽트 상태를 복구
    }
    return false;
  }

  private void logChat(List<GPTMessage> historyMessage) {
    try {
      log.info(objectMapper.writeValueAsString(historyMessage));
    } catch (Exception e) {
      log.info("채팅 로깅 실패");
    }
  }
}
