package com.kuit.agarang.global.s3.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileCategory {
  MEMORY("memory/"), MUSIC("music/");

  private final String path;
}
