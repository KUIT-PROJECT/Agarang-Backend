package com.kuit.agarang.domain.ai.model.dto.typecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TypecastResponse {
  private Result result;

  @Getter
  @NoArgsConstructor
  public static class Result {
    @JsonProperty("speak_v2_url")
    private String speakV2Url;
  }
}
