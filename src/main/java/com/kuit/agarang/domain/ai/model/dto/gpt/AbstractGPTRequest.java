package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class AbstractGPTRequest {
  private final String model = "gpt-4o";
  private final Long temperature = 0L;
  private List<GPTMessage> messages;
  private ResponseFormat responseFormat = null;

  @Getter
  @AllArgsConstructor
  public static class ResponseFormat {
    private String type;
  }

  public AbstractGPTRequest(List<GPTMessage> messages) {
    this.messages = messages;
  }

  public void setRequiredJson(boolean required) {
    if (required) {
      this.responseFormat = new ResponseFormat("json_object");
    } else this.responseFormat = null;
  }
}
