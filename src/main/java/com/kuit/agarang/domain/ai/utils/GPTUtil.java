package com.kuit.agarang.domain.ai.utils;

import com.kuit.agarang.domain.ai.model.dto.gpt.*;
import com.kuit.agarang.domain.ai.model.enums.GPTPrompt;
import com.kuit.agarang.domain.ai.model.enums.GPTRole;
import com.kuit.agarang.domain.ai.model.enums.GPTRoleContent;
import com.kuit.agarang.global.s3.model.dto.S3File;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class GPTUtil {

  public GPTMessage createImageQuestion(GPTPrompt prompt, String imageUrl) {
    return GPTMessage.builder()
      .role(GPTRole.USER)
      .content(List.of(
        GPTContent.createTextContent(prompt), GPTContent.createImageContent(imageUrl)))
      .build();
  }

  public GPTMessage createSystemMessage(GPTRoleContent role) {
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

  public GPTRequest addHistoryMessage(GPTChat gptChat) {
    gptChat.getGptRequest().getMessages().add(getResponseMessage(gptChat));
    return gptChat.getGptRequest();
  }

  public String createKeywordSentence(GPTImageDescription imageDescription) {
    return imageDescription.getNoun().toString() + ", ";
  }

  public String convertToString(S3File s3File) {
    String base64EncodeData = Base64.getEncoder().encodeToString(s3File.getBytes());
    return "data:" + s3File.getContentType().getMimeType() + ";base64," + base64EncodeData;
  }

  public List<GPTMessage> createHistoryMessage(GPTChat gptChat) {
    List<GPTMessage> historyMessage = gptChat.getGptRequest().getMessages();
    historyMessage.add(getResponseMessage(gptChat));
    return historyMessage;
  }

  public String getGPTAnswer(GPTChat gptChat) {
    return (String) getResponseMessage(gptChat).getContent();
  }

  private GPTMessage getResponseMessage(GPTChat gptChat) {
    return gptChat.getGptResponse().getChoices().get(0).getMessage();
  }
}
