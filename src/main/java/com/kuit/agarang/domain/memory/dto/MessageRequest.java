package com.kuit.agarang.domain.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
  // TODO : 텍스트 외의 추가적으로 받을 값이 있는지 확인하기
  private String text;
}
