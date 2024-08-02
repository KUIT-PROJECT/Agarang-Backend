package com.kuit.agarang.domain.home.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.model.entity.Character;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.home.model.dto.HomeResponse;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

  private final BabyRepository babyRepository;
  private final MemoryRepository memoryRepository;
  private final AuthenticationUtil authenticationUtil;

  public HomeResponse getHome() {

    // 오늘 날짜
    LocalDate today = LocalDate.now();

    // 아기 이름
    String providerId = authenticationUtil.getProviderId();
    Baby baby = babyRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));
    String babyName = baby.getName();

    // 디데이
    LocalDate dueDate = baby.getExpectedDueAt();
    Integer dDay = (int) ChronoUnit.DAYS.between(today, dueDate);

    // 캐릭터 이미지
    Character character = baby.getCharacter();
    String characterUrl = character.getImageUrl();

    // 말풍선
    String speechBubble = "안녕!"; // TODO : GPT 아기 말풍선 생성 로직

    // 최근 태교 카드
    List<String> recentImageUrls = memoryRepository.findTop3ImageUrlsByBabyOrderByCreatedAtDesc(baby);


    return HomeResponse.builder()
        .today(today)
        .babyName(babyName)
        .dDay(dDay)
        .characterUrl(characterUrl)
        .speechBubble(speechBubble)
        .memoryUrls(recentImageUrls).build();
  }
}
