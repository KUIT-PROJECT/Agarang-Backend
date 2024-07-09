package com.kuit.agarang.domain.memory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypecastRequest {

  @JsonProperty("actor_id")
  private String actorId;
  private String text;
  private String lang;
  private Double tempo;
  private Integer volume;
  private Integer pitch;
  @JsonProperty("xapi_hd")
  private Boolean xapiHd;
  @JsonProperty("max_seconds")
  private Integer maxSeconds;
  @JsonProperty("model_version")
  private String modelVersion;
  @JsonProperty("xapi_audio_format")
  private String xapiAudioFormat;

  public static TypecastRequest create(MessageRequest request) {
    return TypecastRequest.builder()
      // TODO : 전체 고정값인지 확인하기
      .actorId("5ffda49bcba8f6d3d46fc447")
      .text(request.getText())
      .lang("auto")
      .tempo(1.0)
      .volume(100)
      .pitch(0)
      .xapiHd(true)
      .maxSeconds(60)
      .modelVersion("latest")
      .xapiAudioFormat("wav")
      .build();
  }
}
