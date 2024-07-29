package com.kuit.agarang.domain.ai.model.entity.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTImageDescription;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GPTChatHistory {
  private String imageTempPath;
  private GPTImageDescription imageDescription;
  private String questionAudioUrl;
  private String historyMessage;

  @Builder
  public GPTChatHistory(String imageTempPath, GPTImageDescription imageDescription, String questionAudioUrl, List<GPTMessage> historyMessages) {
    this.imageTempPath = imageTempPath;
    this.imageDescription = imageDescription;
    this.questionAudioUrl = questionAudioUrl;

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      this.historyMessage = objectMapper.writeValueAsString(historyMessages);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
