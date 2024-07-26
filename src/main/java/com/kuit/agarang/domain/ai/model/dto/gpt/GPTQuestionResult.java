package com.kuit.agarang.domain.ai.model.dto.gpt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GPTQuestionResult {
  private String questionId;
  private String questionText;
  private String questionAudioUrl;

  @Builder
  public GPTQuestionResult(String questionId, String questionText, String questionAudioUrl) {
    this.questionId = questionId;
    this.questionText = questionText;
    this.questionAudioUrl = questionAudioUrl;
  }
}
