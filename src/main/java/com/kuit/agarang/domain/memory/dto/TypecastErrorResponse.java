package com.kuit.agarang.domain.memory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TypecastErrorResponse {
  private Message message;

  @Getter
  @NoArgsConstructor
  public static class Message {
    private String msg;
    private String errorCode;
  }
}