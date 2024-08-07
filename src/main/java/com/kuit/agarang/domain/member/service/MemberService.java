package com.kuit.agarang.domain.member.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.common.utils.CodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_BABY;
import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final BabyRepository babyRepository;

  public void verifyBabyCode(String providerId, String babyCode) {

    Baby baby = babyRepository.findByBabyCode(babyCode)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    member.addBaby(baby);
  }

  public void processBabyAssignment(String providerId, String babyName, LocalDate dueDate, String familyRole) {

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    if (member.getBaby() == null) {

      String babyCode;
      do {
        babyCode = CodeUtil.generateUniqueCode();
      } while (babyRepository.existsByBabyCode(babyCode));

      Baby baby = Baby.builder()
          .name(babyName)
          .expectedDueAt(dueDate)
          .babyCode(babyCode)
          .build();

      babyRepository.save(baby);
      member.addBaby(baby);
    }

    memberRepository.updateFamilyRoleByProviderId(providerId, familyRole);
  }
}
