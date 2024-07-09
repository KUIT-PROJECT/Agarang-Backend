package com.kuit.agarang.domain.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TypecastErrorResponse {
  private Message message;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Message {
    private String msg;
    private String errorCode;
  }
}