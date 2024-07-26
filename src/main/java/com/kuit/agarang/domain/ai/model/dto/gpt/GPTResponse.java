package com.kuit.agarang.domain.ai.model.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponse {
  private String id;
  private String object;
  private Long created;
  private List<GPTResultChoice> choices;
  private GPTUsage usage;
}
