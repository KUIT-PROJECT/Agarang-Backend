package com.kuit.agarang.domain.ai.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum GPTRoleContent {
  IMAGE_DESCRIBER("You're the one who describes the image. Emotionally describe the image"),
  COUNSELOR("너는 상담사야.");

  private String text;
}
