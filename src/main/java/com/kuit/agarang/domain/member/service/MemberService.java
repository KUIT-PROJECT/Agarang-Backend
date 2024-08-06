package com.kuit.agarang.domain.member.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_BABY;
import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.NOT_FOUND_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final BabyRepository babyRepository;
  private final AuthenticationUtil authenticationUtil;

  public void verifyBabyCode(String providerId, String babyCode) {

    log.info("providerId = {}", providerId);

    Baby baby = babyRepository.findByCode(babyCode)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    member.setBaby(baby);
  }

  public void assignFamilyRole(String familyRole) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    memberRepository.updateFamilyRoleByProviderId(providerId, familyRole);
  }

  public void saveNewBaby(String babyName, LocalDate dueDate) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    Baby baby = Baby.builder()
        .name(babyName)
        .expectedDueAt(dueDate)
        .build();

    member.setBaby(baby);
    babyRepository.save(baby);
  }


  public void saveBabyName(String babyName) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    Baby baby = Optional.ofNullable(member.getBaby())
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    baby.setName(babyName);
  }

  public void saveBabyDueDate(LocalDate dueDate) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

    Baby baby = Optional.ofNullable(member.getBaby())
        .orElseThrow(() -> new BusinessException(NOT_FOUND_BABY));

    baby.setExpectedDueAt(dueDate);
  }
}
