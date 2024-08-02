package com.kuit.agarang.domain.member.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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
        .orElseThrow(() -> new RuntimeException("Baby not found"));

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new RuntimeException("Member not found"));

    member.setBaby(baby);
  }

  public void assignFamilyRole(String familyRole) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    memberRepository.updateFamilyRoleByProviderId(providerId, familyRole);
  }

  public void saveBabyName(String babyName) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new RuntimeException("Member not found"));

    Baby baby = Optional.ofNullable(member.getBaby())
        .orElseThrow(() -> new RuntimeException("Baby not found"));

    baby.setName(babyName);
  }

  public void saveBabyDueDate(LocalDate dueDate) {

    String providerId = authenticationUtil.getProviderId();
    log.info("providerId = {}", providerId);

    Member member = memberRepository.findByProviderId(providerId)
        .orElseThrow(() -> new RuntimeException("Member not found"));

    Baby baby = Optional.ofNullable(member.getBaby())
        .orElseThrow(() -> new RuntimeException("Baby not found"));

    baby.setExpectedDueAt(dueDate);
  }
}
