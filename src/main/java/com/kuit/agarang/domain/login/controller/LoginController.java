package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.member.model.dto.BabyCodeRequest;
import com.kuit.agarang.domain.member.model.dto.BabyDueDateRequest;
import com.kuit.agarang.domain.member.model.dto.BabyNameRequest;
import com.kuit.agarang.domain.member.model.dto.FamilyRoleRequest;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

  private final MemberService memberService;

  /**
   * 아기 코드 검증
   * TODO : 아기 코드 생성 로직
   */
  @PostMapping("/babycode")
  public ResponseEntity<String> verifyBabyCode(@RequestBody BabyCodeRequest request) {
    String babyCode = memberService.verifyBabyCode(request.getProviderId(), request.getBabyCode());
    return ResponseEntity.ok(babyCode);
  }

  /**
   * 가족 역할 등록
   */
  @PostMapping("/familyrole")
  public ResponseEntity<Void> assignFamilyRole(@RequestBody FamilyRoleRequest request) {
    Member updatedMember = memberService.assignFamilyRole(request.getProviderId(), request.getFamilyRole());
    return ResponseEntity.ok().build();
  }

  /**
   * 태명 등록
   */
  @PostMapping("/babyname")
  public ResponseEntity<Void> saveBabyName(@RequestBody BabyNameRequest request) {
    memberService.saveBabyName(request.getProviderId(), request.getBabyName());
    return ResponseEntity.ok().build();
  }

  /**
   * 출산 예정일 등록
   */
  @PostMapping("/babyduedate")
  public ResponseEntity<Void> saveBabyDueDate(@RequestBody BabyDueDateRequest request) {
    memberService.saveBabyDueDate(request.getProviderId(), request.getDate());
    return ResponseEntity.ok().build();
  }
}
