package com.kuit.agarang.domain.ai.model.entity.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GPTChatHistory {
  private String id;
  private String imageTempPath;
  private String musicTitle;
  private List<String> hashtags;
  private String questionAudioUrl;
  private String historyMessage;

  @Builder
  public GPTChatHistory(String id, String imageTempPath, String musicTitle, List<String> hashtags, String questionAudioUrl) {
    this.id = id;
    this.imageTempPath = imageTempPath;
    this.musicTitle = musicTitle;
    this.hashtags = hashtags;
    this.questionAudioUrl = questionAudioUrl;
  }

  public void putHistoryMessage(List<GPTMessage> messages) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      this.historyMessage = objectMapper.writeValueAsString(messages);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
