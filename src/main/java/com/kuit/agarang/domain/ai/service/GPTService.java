package com.kuit.agarang.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.gpt.*;
import com.kuit.agarang.domain.ai.model.enums.GPTPrompt;
import com.kuit.agarang.domain.ai.model.enums.GPTRoleContent;
import com.kuit.agarang.domain.ai.utils.GPTRequestUtil;
import com.kuit.agarang.domain.ai.utils.GptClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GPTService {

  private final GptClientUtil gptClientUtil;
  private final GPTRequestUtil gptRequestUtil;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public GPTChat getImageDescription(String imageUrl) {
    GPTMessage systemMessage = gptRequestUtil.createSystemMessage(GPTRoleContent.IMAGE_DESCRIBER);
    GPTMessage imageQuestion = gptRequestUtil.createImageQuestion(GPTPrompt.IMAGE_DESCRIPTION, imageUrl);

    GPTRequest request = new GPTRequest(List.of(systemMessage, imageQuestion));
    request.setRequiredJson(true);
    GPTResponse response = gptClientUtil.post(request, GPTResponse.class);
    GPTChat chat = new GPTChat(request, response);

    // TODO : gpt 개발 끝나면 지우기
    logChat(chat);
    return chat;
  }

  public GPTChat createFirstQuestion(GPTImageDescription imageDescription) {
    GPTMessage systemMessage = gptRequestUtil.createSystemMessage(GPTRoleContent.COUNSELOR);
    String keywordSentence = gptRequestUtil.createKeywordSentence(imageDescription);
    GPTMessage message =
      gptRequestUtil.createTextMessage(keywordSentence + GPTPrompt.FIRST_QUESTION.getText());

    GPTRequest request = new GPTRequest(new ArrayList<>(List.of(systemMessage, message)));
    request.setRequiredJson(false);
    GPTResponse response = gptClientUtil.post(request, GPTResponse.class);
    GPTChat chat = new GPTChat(request, response);

    // TODO : gpt 개발 끝나면 지우기
    logChat(chat);
    return chat;
  }

  private void logChat(GPTChat gptChat) {
    String json = null;
    try {
      json = objectMapper.writeValueAsString(gptChat);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    log.info(json);
  }
}
