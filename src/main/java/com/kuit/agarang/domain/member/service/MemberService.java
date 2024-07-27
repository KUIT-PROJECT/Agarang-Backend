package com.kuit.agarang.domain.member.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
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
public class MemberService {

  private final MemberRepository memberRepository;
  private final BabyRepository babyRepository;

  @Transactional
  public String verifyBabyCode(String providerId, String babyCode) {
    Baby baby = babyRepository.findByCode(babyCode);

    if (baby == null) {
      throw new RuntimeException("Baby not found");
    }

    Optional<Member> optionalMember = memberRepository.findByProviderId(providerId);
    if (optionalMember.isEmpty()) {
      throw new RuntimeException("Member not found");
    }

    Member member = optionalMember.get();
    Member updatedMember = member.changeBaby(baby);
    memberRepository.save(updatedMember);

    return baby.getCode();
  }

  @Transactional
  public Member assignFamilyRole(String providerId, String familyRole) {
    memberRepository.updateFamilyRoleByProviderId(providerId, familyRole);
    Optional<Member> optionalMember = memberRepository.findByProviderId(providerId);
    if (optionalMember.isEmpty()) {
      throw new RuntimeException("Member not found after update");
    }
    return optionalMember.get();
  }

  @Transactional
  public void saveBabyName(String providerId, String babyName) {
    Optional<Member> optionalMember = memberRepository.findByProviderId(providerId);
    if (optionalMember.isEmpty()) {
      throw new RuntimeException("Member not found");
    }

    Member member = optionalMember.get();
    Baby baby = member.getBaby();
    if (baby == null) {
      throw new RuntimeException("Baby not found");
    }
    baby.setName(babyName);
    babyRepository.save(baby);
  }

  @Transactional
  public void saveBabyDueDate(String providerId, LocalDate dueDate) {
    Optional<Member> optionalMember = memberRepository.findByProviderId(providerId);
    if (optionalMember.isEmpty()) {
      throw new RuntimeException("Member not found");
    }

    Member member = optionalMember.get();
    Baby baby = member.getBaby();
    if (baby == null) {
      throw new RuntimeException("Baby not found");
    }
    baby.setExpectedDueAt(dueDate);
    babyRepository.save(baby);
  }
}
