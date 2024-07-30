package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTImageDescription {
  private String text;
  private List<String> noun;

  public static GPTImageDescription from(String content) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(content, GPTImageDescription.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
