package com.kuit.agarang.domain.home.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.model.entity.Character;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.baby.repository.CharacterRepository;
import com.kuit.agarang.domain.home.model.dto.CharacterSettingResponse;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterService {
  
  private final CharacterRepository characterRepository;
  private final BabyRepository babyRepository;

  public List<CharacterSettingResponse> getCharactersByDate(Long memberId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));

    long dDay = ChronoUnit.DAYS.between(LocalDate.now(), baby.getDueDate());
    Integer level = dDay <= 140 ? 2 : 1;

    List<Character> charactersByLevel = characterRepository.findByLevel(level);

    return charactersByLevel.stream()
        .map(character -> new CharacterSettingResponse(character.getId(), character.getName(),
            character.getDescription(), character.getImageUrl()))
        .collect(Collectors.toList());
  }

  public void updateCharacterSetting(Long memberId, Long characterId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));

    Character character = characterRepository.findById(characterId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_CHARACTER));

    baby.setCharacter(character);

    babyRepository.save(baby);
  }
}
