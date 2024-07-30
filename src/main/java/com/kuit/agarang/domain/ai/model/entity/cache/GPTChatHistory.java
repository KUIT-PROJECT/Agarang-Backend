package com.kuit.agarang.domain.ai.model.entity.cache;

import com.kuit.agarang.domain.ai.model.dto.gpt.GPTMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GPTChatHistory {
  private String imageTempPath;
  private List<String> hashtags;
  private List<GPTMessage> historyMessage;

  @Builder
  public GPTChatHistory(String imageTempPath, List<String> hashtags, List<GPTMessage> historyMessages) {
    this.imageTempPath = imageTempPath;
    this.hashtags = hashtags;
    this.historyMessage = historyMessages;
  }
}
