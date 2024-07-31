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

  public String convert(S3File s3File) {
    String base64EncodeData = Base64.getEncoder().encodeToString(s3File.getBytes());
    return "data:" + s3File.getContentType().getMimeType() + ";base64," + base64EncodeData;
  }

  public String convert(String familyRole, String babyName) {
    return "위의 대화는 오늘 " + familyRole + "에게 있었던 일이야. " +
      familyRole + "에게 있었던 일을 기반으로 " +
      familyRole + "가 태아인 " + babyName + "에게 편지를 작성할거야. " + GPTPrompt.MEMORY_TEXT.getText();
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
