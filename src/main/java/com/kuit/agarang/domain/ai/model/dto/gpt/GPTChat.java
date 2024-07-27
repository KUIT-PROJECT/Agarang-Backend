package com.kuit.agarang.domain.ai.model.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GPTChat {
  private GPTRequest gptRequest;
  private GPTResponse gptResponse;

  public GPTMessage getResponseMessage() {
    return this.gptResponse.getChoices().get(0).getMessage();
  }
}
