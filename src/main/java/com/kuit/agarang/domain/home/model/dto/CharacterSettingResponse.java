package com.kuit.agarang.domain.home.model.dto;

import com.kuit.agarang.domain.baby.model.entity.Character;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CharacterSettingResponse {

  private Long characterId;
  private String name;
  private String description;
  private String imageUrl;

  @Builder
  public CharacterSettingResponse(Long characterId, String name, String description, String imageUrl) {
    this.characterId = characterId;
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
  }

  public static CharacterSettingResponse from(Character character, int level) {
    return CharacterSettingResponse.builder()
      .characterId(character.getId())
      .name(character.getName())
      .description(character.getDescription())
      .imageUrl(level == 1 ? character.getImageUrl() : character.getSecondImageUrl())
      .build();
  }
}
