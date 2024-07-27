package com.kuit.agarang.domain.ai.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum GPTPrompt {
  IMAGE_DESCRIPTION("이미지를 한국어로 하나의 문장으로 감정적으로 묘사해줘. " +
    "text는 하나의 문장으로 묘사된 내용을 포함하고, " +
    "noun는 해당 이미지를 기반으로 인스타 태그로 사용할 수 있는 명사나 형용사를 의미가 중복되지 않게 최대 3개 포함해줘. " +
    "출력은 json 파일 형태로 해줘. "),

  FIRST_QUESTION("키워드 중 하나를 선택해서 사용자에게 오늘의 주제에 대해서 말해주고, " +
    "사용자의 오늘 추억을 회상시킬 수 있는 질문을 한 문장으로 짧게 자연스럽게 작성해줘. " +
    "말투는 아이가 부모님에게 묻는 말투로 해줘. ");

  private String text;
}
