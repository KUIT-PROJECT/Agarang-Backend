package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTResultChoice {
  private Integer index;
  private GPTMessage message;
  @JsonProperty("finish_reason")
  private String finishReason;
}
