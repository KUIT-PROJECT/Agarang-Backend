package com.kuit.agarang.domain.memory.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyMemoryRequest {
  private String id;
  private String text;
}
