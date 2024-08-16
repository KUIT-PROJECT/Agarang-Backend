package com.kuit.agarang.domain.home.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.home.model.dto.BabySettingResponse;
import com.kuit.agarang.domain.home.model.dto.BabySettingUpdateRequest;
import com.kuit.agarang.domain.home.model.dto.FamilySettingResponse;
import com.kuit.agarang.domain.home.model.dto.GlobalSettingResponse;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingService {

  private final BabyRepository babyRepository;

  public GlobalSettingResponse getGlobalSetting(Long memberId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));
    String babyName = baby.getName();

    LocalDate dueDate = baby.getDueDate();

    LocalDate today = LocalDate.now();
    Integer dDay = (int) ChronoUnit.DAYS.between(today, dueDate);

    return GlobalSettingResponse.builder()
        .babyName(babyName)
        .dDay(dDay)
        .dueDate(dueDate).build();
  }

  public BabySettingResponse getBabySetting(Long memberId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));
    String babyName = baby.getName();

    LocalDate dueDate = baby.getDueDate();

    Double weight = baby.getWeight();

    return BabySettingResponse.builder()
        .babyName(babyName)
        .dueDate(dueDate)
        .weight(weight).build();
  }

  @Transactional
  public void updateBabySetting(Long memberId, BabySettingUpdateRequest updateRequest) {
    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));

    Optional.ofNullable(updateRequest.getBabyName())
        .ifPresent(baby::setName);

    Optional.ofNullable(updateRequest.getDueDate())
        .ifPresent(baby::setDueDate);

    Optional.ofNullable(updateRequest.getWeight())
        .ifPresent(baby::setWeight);

    babyRepository.save(baby);
  }

  public FamilySettingResponse getFamilySetting(Long memberId) {

    // 아기 코드
    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));
    String babyName = baby.getBabyCode();

    // 가족
    List<MemberDTO> memberDTOs = baby.getMembers().stream()
        .map(member -> MemberDTO.builder()
            .memberId(memberId)
            .name(member.getName())
            .role(member.getRole())
            .providerId(member.getProviderId())
            .build())
        .collect(Collectors.toList());

    return FamilySettingResponse.builder()
        .babyCode(babyName)
        .members(memberDTOs).build();
  }

}
