package com.kuit.agarang.domain.ai.model.dto.musicGen;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MusicGenRequest {
  private String version;
  private String webhook;
  private List<String> webhookEventsFilter;
  private Input input;

  @Getter
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @Builder
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Input {
    private String prompt;
    private Integer duration;
    private String modelVersion;
    private String outputFormat;
    private String normalizationStrategy;
  }

  public MusicGenRequest(String version, String webhookUrl, String prompt, Integer duration) {
    this.version = version;
    this.webhook = webhookUrl;
    this.webhookEventsFilter = List.of("completed");
    this.input = Input.builder()
      .prompt(prompt)
      .duration(duration)
      .modelVersion("stereo-large")
      .outputFormat("mp3")
      .normalizationStrategy("peak")
      .build();
  }
}