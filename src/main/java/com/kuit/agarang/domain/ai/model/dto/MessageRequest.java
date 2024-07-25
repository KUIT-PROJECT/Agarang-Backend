package com.kuit.agarang.domain.ai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
  // TODO : actor 정보 추가 (남아 or 여아)
  private String text;
}
