package com.kuit.agarang.domain.member.service;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_BABY;
import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_CHARACTER;
import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_MEMBER;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.baby.repository.CharacterRepository;
import com.kuit.agarang.domain.member.model.dto.BabyCodeRequest;
import com.kuit.agarang.domain.member.model.dto.ProcessBabyRequest;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.utils.CodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final BabyRepository babyRepository;
  private final CharacterRepository characterRepository;

  public void verifyBabyCode(Long memberId, BabyCodeRequest request) {

    Baby baby = babyRepository.findByCode(request.getBabyCode())
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    member.setFamilyRole(request.getFamilyRole());
    member.addBaby(baby);
  }

  public void processBabyAssignment(Long memberId, ProcessBabyRequest request) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    if (member.getBaby() == null) {

      Baby baby = Baby.builder()
          .name(request.getBabyName())
          .dueDate(request.getDueDate())
          .weight(1.4) // Default Value
          .code(CodeUtil.generateUniqueCode())
          .build();

      // Default Value
      baby.setCharacter(characterRepository.findById(6L)
          .orElseThrow(() -> new BusinessException(NOT_FOUND_CHARACTER)));

      babyRepository.save(baby);
      member.addBaby(baby);
    }

    member.setFamilyRole(request.getFamilyRole());
  }
}
