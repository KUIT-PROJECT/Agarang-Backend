package com.kuit.agarang.domain.memory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TypecastResponse {
  private Result result;

  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Result {
    @JsonProperty("speak_v2_url")
    private String speakV2Url;
  }
}
