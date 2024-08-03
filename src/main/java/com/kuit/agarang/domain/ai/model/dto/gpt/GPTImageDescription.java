package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.global.common.exception.exception.OpenAPIException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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
      log.info("gpt's image description : {}", content);
      throw new OpenAPIException(BaseResponseStatus.INVALID_GPT_RESPONSE);
    }
  }
}
