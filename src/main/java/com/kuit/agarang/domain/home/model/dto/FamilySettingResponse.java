package com.kuit.agarang.domain.home.model.dto;

import com.kuit.agarang.domain.member.model.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FamilySettingResponse {

  private String babyCode;
  private List<Member> members;

}
