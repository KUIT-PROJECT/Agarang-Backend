package com.kuit.agarang.domain.ai.utils;

import com.kuit.agarang.domain.ai.model.dto.gpt.GPTChat;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTContent;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import com.kuit.agarang.domain.ai.model.enums.GPTRole;
import com.kuit.agarang.domain.ai.model.enums.GPTSystemRole;
import com.kuit.agarang.global.common.exception.exception.OpenAPIException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GPTUtil {

  public GPTMessage createImageQuestion(String prompt, String imageUrl) {
    return GPTMessage.builder()
      .role(GPTRole.USER)
      .content(List.of(
        GPTContent.createTextContent(prompt), GPTContent.createImageContent(imageUrl)))
      .build();
  }

  public GPTMessage createSystemMessage(GPTSystemRole role) {
    return GPTMessage.builder()
      .role(GPTRole.SYSTEM)
      .content(role.getText())
      .build();
  }

  public GPTMessage createTextMessage(String prompt) {
    return GPTMessage.builder()
      .role(GPTRole.USER)
      .content(prompt)
      .build();
  }

  public List<GPTMessage> createHistoryMessage(GPTChat gptChat) {
    List<GPTMessage> historyMessage = gptChat.getGptRequest().getMessages();
    historyMessage.add(getResponseMessage(gptChat));
    return historyMessage;
  }

  public String getGPTAnswer(GPTChat gptChat) {
    try {
      return (String) getResponseMessage(gptChat).getContent();
    } catch (NullPointerException e) {
      throw new OpenAPIException(BaseResponseStatus.INVALID_GPT_RESPONSE);
    }
  }

  private GPTMessage getResponseMessage(GPTChat gptChat) {
    try {
      return gptChat.getGptResponse().getChoices().get(0).getMessage();
    } catch (NullPointerException e) {
      throw new OpenAPIException(BaseResponseStatus.INVALID_GPT_RESPONSE);
    }
  }
}
