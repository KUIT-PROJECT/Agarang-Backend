package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.gpt.*;
import com.kuit.agarang.domain.ai.model.enums.GPTPrompt;
import com.kuit.agarang.domain.ai.model.enums.GPTRoleContent;
import com.kuit.agarang.domain.ai.utils.GPTUtil;
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
  private final GPTUtil gptUtil;

  public GPTChat getImageDescription(String imageUrl) {
    GPTMessage systemMessage = gptUtil.createSystemMessage(GPTRoleContent.IMAGE_DESCRIBER);
    GPTMessage imageQuestion = gptUtil.createImageQuestion(GPTPrompt.IMAGE_DESCRIPTION, imageUrl);

    GPTRequest request = new GPTRequest(List.of(systemMessage, imageQuestion));
    request.setRequiredJson(true);
    GPTResponse response = gptClientUtil.post(request, GPTResponse.class);
    return new GPTChat(request, response);
  }

  public GPTChat createImageQuestion(GPTImageDescription imageDescription) {
    GPTMessage systemMessage = gptUtil.createSystemMessage(GPTRoleContent.COUNSELOR);
    String keywordSentence = gptUtil.createKeywordSentence(imageDescription);
    GPTMessage message =
      gptUtil.createTextMessage(keywordSentence + GPTPrompt.IMAGE_QUESTION.getText());

    GPTRequest request = new GPTRequest(new ArrayList<>(List.of(systemMessage, message)));
    GPTResponse response = gptClientUtil.post(request, GPTResponse.class);
    return new GPTChat(request, response);
  }

  public GPTChat createNextQuestion(List<GPTMessage> historyMessage, String text) {
    historyMessage.add(gptUtil.createTextMessage(text));
    GPTRequest request = new GPTRequest(historyMessage);
    GPTResponse response = gptClientUtil.post(request, GPTResponse.class);
    return new GPTChat(request, response);
  }
}
