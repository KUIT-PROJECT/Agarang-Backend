package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GPTRequest extends AbstractGPTRequest {
  public GPTRequest(List<GPTMessage> messages) {
    super(messages);
  }

  @Override
  public void setRequiredJson(boolean required) {
    super.setRequiredJson(required);
  }
}
