package com.kuit.agarang.domain.login.controller;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.login.utils.AuthenticationUtil;
import com.kuit.agarang.domain.member.model.dto.*;
import com.kuit.agarang.domain.member.service.MemberService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
   * 새 아기 등록
   */
  @PostMapping("new-baby")
  public ResponseEntity<BaseResponse<Void>> assignNewBaby(@AuthenticationPrincipal CustomOAuth2User details,
                                                          @RequestBody NewBabyRequest request) {
    memberService.saveNewBaby(details.getProviderId(), request.getBabyName(), request.getDueDate());
    return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
  }

}
