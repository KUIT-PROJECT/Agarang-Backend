package com.kuit.agarang.domain.ai.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryTextInfo {
  private String babyName;
  private String familyRole;

  @Builder
  public MemoryTextInfo(String babyName, String familyRole) {
    this.babyName = babyName;
    this.familyRole = familyRole;
  }
}
