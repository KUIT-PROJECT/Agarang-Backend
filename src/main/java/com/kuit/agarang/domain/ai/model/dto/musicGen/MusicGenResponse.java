package com.kuit.agarang.domain.ai.model.dto.musicGen;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MusicGenResponse {
  private String error;
  private String id;
  private String output;
  private String status;
  private String startedAt;
  private String completedAt;
}