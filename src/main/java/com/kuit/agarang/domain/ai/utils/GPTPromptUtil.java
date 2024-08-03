package com.kuit.agarang.domain.ai.utils;

import com.kuit.agarang.domain.ai.model.dto.gpt.GPTImageDescription;
import com.kuit.agarang.domain.baby.model.entity.Character;
import org.springframework.stereotype.Component;

@Component
public class GPTPromptUtil {

  public String createImageDescriptionPrompt() {
    return new StringBuilder("이미지를 한국어로 하나의 문장으로 감정적으로 묘사해줘. ")
      .append("text는 하나의 문장으로 묘사된 내용을 포함하고, ")
      .append("noun는 해당 이미지를 기반으로 인스타 태그로 사용할 수 있는 명사나 형용사를 의미가 중복되지 않게 최대 3개 포함해줘. ")
      .append("출력은 json 파일 형태로 해줘. ").toString();
  }

  public String createImageQuestionPrompt(GPTImageDescription imageDescription) {
    String imageDescriptionKeyword = imageDescription.getNoun().toString();
    return new StringBuilder(imageDescriptionKeyword)
      .append(", 키워드 중 하나를 선택해서 사용자에게 오늘의 주제에 대해서 말해주고, ")
      .append("사용자의 오늘 추억을 회상시킬 수 있는 질문을 한 문장으로 짧게 자연스럽게 작성해줘. ")
      .append("말투는 아이가 부모님에게 묻는 말투로 해줘. ").toString();
  }

  public String createMemoryTextPrompt(String babyName, String familyRole) {
    return new StringBuilder("위의 대화는 오늘 ").append(familyRole).append("에게 있었던 일이야. ")
      .append(familyRole).append("에게 있었던 일을 기반으로 ")
      .append(familyRole).append("가 태아인 ").append(babyName).append("에게 편지를 작성할거야. ")
      .append("위의 대화를 바탕으로 일상 태담을 5문장 정도로 감성적으로 작성해줘.").toString();
  }

  public String createCharacterBubble(Character character, String familyRole) {
    return new StringBuilder("귀여운 아기 ").append(character.getName()).append(" 캐릭터 특징이나 울음소리, 외모 등을 기반으로 맞춰서 ")
      .append(familyRole).append("에게 하고 싶은 말을 10자 내로 짧게 작성해줘. ")
      .append(character.getName()).append("의 특징은 다음과 같아. ").append(character.getDescription()).toString();
  }
}
