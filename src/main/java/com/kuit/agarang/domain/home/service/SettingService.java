package com.kuit.agarang.domain.home.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.home.model.dto.BabySettingResponse;
import com.kuit.agarang.domain.home.model.dto.BabySettingUpdateRequest;
import com.kuit.agarang.domain.home.model.dto.FamilySettingResponse;
import com.kuit.agarang.domain.home.model.dto.GlobalSettingResponse;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
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

    LocalDate dueDate = baby.getDueDate();
    Integer dDay = (int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

    return GlobalSettingResponse.builder()
        .characterImageUrl(baby.getCharacter().getImageUrl())
        .babyName(baby.getName())
        .dDay(dDay)
        .dueDate(dueDate).build();
  }

  public BabySettingResponse getBabySetting(Long memberId) {

    Baby baby = babyRepository.findByMemberId(memberId)
        .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_BABY));

    return BabySettingResponse.builder()
        .babyName(baby.getName())
        .dueDate(baby.getDueDate())
        .weight(baby.getWeight()).build();
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
        .babyCode(baby.getName())
        .members(memberDTOs).build();
  }

}
