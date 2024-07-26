package com.kuit.agarang.domain.ai.model.dto.typecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TypecastWebhookResponse {
  private String status;
  @JsonProperty("audio_download_url")
  private String audioDownloadUrl;
}