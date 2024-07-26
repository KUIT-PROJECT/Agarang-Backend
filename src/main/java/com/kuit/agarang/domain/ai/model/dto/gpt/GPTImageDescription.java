package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GPTImageDescription {
  private String text;
  private List<String> noun;

  public static GPTImageDescription from(Object content) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue((String) content, GPTImageDescription.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
