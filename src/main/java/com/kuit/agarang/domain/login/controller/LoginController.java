package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.dto.BabyCodeRequest;
import com.kuit.agarang.domain.member.model.dto.BabyDueDateRequest;
import com.kuit.agarang.domain.member.model.dto.BabyNameRequest;
import com.kuit.agarang.domain.member.model.dto.FamilyRoleRequest;
import com.kuit.agarang.domain.member.service.MemberService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

  private final MemberService memberService;
  private final AuthenticationUtil authenticationUtil;

  /**
   * 아기 코드 검증
   * TODO : 아기 코드 생성 로직
   */
  @PostMapping("/baby-code")
  public ResponseEntity<BaseResponse<Void>> verifyBabyCode(@RequestBody BabyCodeRequest request) {
    String providerId = authenticationUtil.getProviderId();
    memberService.verifyBabyCode(providerId, request.getBabyCode());
    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }

  /**
   * 가족 역할 등록
   */
  @PostMapping("/family-role")
  public ResponseEntity<BaseResponse<Void>> assignFamilyRole(@RequestBody FamilyRoleRequest request) {
    memberService.assignFamilyRole(request.getFamilyRole());
    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }

  /**
   * 태명 등록
   */
  @PostMapping("/baby-name")
  public ResponseEntity<BaseResponse<Void>> saveBabyName(@RequestBody BabyNameRequest request) {
    memberService.saveBabyName(request.getBabyName());
    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }

  /**
   * 출산 예정일 등록
   */
  @PostMapping("/due-date")
  public ResponseEntity<BaseResponse<Void>> saveBabyDueDate(@RequestBody BabyDueDateRequest request) {
    memberService.saveBabyDueDate(request.getDate());
    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }
}
