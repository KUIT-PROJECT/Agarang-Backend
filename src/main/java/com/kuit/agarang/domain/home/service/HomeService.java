package com.kuit.agarang.domain.home.service;

import com.kuit.agarang.domain.ai.service.AIService;
import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.home.model.dto.HomeResponse;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.memory.model.dto.MemoryImageDTO;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

  private final BabyRepository babyRepository;
  private final MemberRepository memberRepository;
  private final MemoryRepository memoryRepository;
  private final AIService aiService;
  private final CharacterService characterService;

  public HomeResponse getHome(Long memberId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));

    LocalDate today = LocalDate.now();
    LocalDate dueDate = baby.getDueDate();
    Integer dDay = (int) ChronoUnit.DAYS.between(today, dueDate);

    String characterUrl = characterService.getCharacterImage(baby);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));
    String speechBubble = aiService.getCharacterBubble(baby.getCharacter(), member.getFamilyRole());

    List<MemoryImageDTO> resentMemories =
      memoryRepository.findTop3ByBabyOrderByCreatedAtDesc(baby).stream().map(MemoryImageDTO::from).toList();

    return HomeResponse.builder()
        .today(today)
        .babyName(baby.getName())
        .dDay(dDay)
        .characterUrl(characterUrl)
        .speechBubble(speechBubble)
        .memories(resentMemories).build();
  }
}
