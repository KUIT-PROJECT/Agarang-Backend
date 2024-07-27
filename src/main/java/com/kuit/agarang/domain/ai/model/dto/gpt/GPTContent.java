package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuit.agarang.domain.ai.model.enums.GPTContentType;
import com.kuit.agarang.domain.ai.model.enums.GPTPrompt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GPTContent {
  private String type;
  private String text;
  @JsonProperty("image_url")
  private ImageUrlContent imageUrl;

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ImageUrlContent {
    private String url;
  }

  public static GPTContent createTextContent(GPTPrompt prompt) {
    return new GPTContent(GPTContentType.TEXT.toLowerString(), prompt.getText(), null);
  }

  public static GPTContent createImageContent(String imageUrl) {
    return new GPTContent(GPTContentType.IMAGE_URL.toLowerString(), null, new ImageUrlContent(imageUrl));
  }
}
