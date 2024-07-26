package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GPTUsage {
  @JsonProperty("prompt_tokens")
  long promptTokens;
  @JsonProperty("completion_tokens")
  long completionTokens;
  @JsonProperty("total_tokens")
  long totalTokens;
}
