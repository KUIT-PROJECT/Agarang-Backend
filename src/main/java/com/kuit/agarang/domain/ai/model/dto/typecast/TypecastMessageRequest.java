package com.kuit.agarang.domain.ai.model.dto.typecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypecastMessageRequest {
  // TODO : actor 정보 추가 (남아 or 여아)
  private String text;
}
