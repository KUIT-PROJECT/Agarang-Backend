package com.kuit.agarang.domain.member.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.baby.repository.CharacterRepository;
import com.kuit.agarang.domain.member.model.dto.ProcessBabyRequest;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.utils.CodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final BabyRepository babyRepository;
  private final CharacterRepository characterRepository;

  public void verifyBabyCode(Long id, String babyCode) {

    Baby baby = babyRepository.findByBabyCode(babyCode)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    member.addBaby(baby);
  }

  public void processBabyAssignment(Long id, ProcessBabyRequest request) {

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    if (member.getBaby() == null) {

      Baby baby = Baby.builder()
          .name(request.getBabyName())
          .dueDate(request.getDueDate())
          .weight(1.0) // Default Value
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
