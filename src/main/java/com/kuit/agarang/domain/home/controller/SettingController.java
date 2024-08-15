package com.kuit.agarang.domain.home.controller;

import com.kuit.agarang.domain.home.model.dto.BabySettingResponse;
import com.kuit.agarang.domain.home.model.dto.BabySettingUpdateRequest;
import com.kuit.agarang.domain.home.model.dto.FamilySettingResponse;
import com.kuit.agarang.domain.home.model.dto.GlobalSettingResponse;
import com.kuit.agarang.domain.home.service.SettingService;
import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home/setting")
@RequiredArgsConstructor
public class SettingController {

  private final SettingService settingService;

  @GetMapping()
  public ResponseEntity<BaseResponse<GlobalSettingResponse>> getSetting(@AuthenticationPrincipal CustomOAuth2User details) {
    GlobalSettingResponse settingData = settingService.getGlobalSetting(details.getProviderId());
    return ResponseEntity.ok(new BaseResponse<>(settingData));
  }

  @GetMapping("/baby")
  public ResponseEntity<BaseResponse<BabySettingResponse>> getBabySetting(@AuthenticationPrincipal CustomOAuth2User details) {
    BabySettingResponse settingData = settingService.getBabySetting(details.getProviderId());
    return ResponseEntity.ok(new BaseResponse<>(settingData));
  }

  @PutMapping("/baby")
  public ResponseEntity<BaseResponse<Void>> updateBabySetting(@AuthenticationPrincipal CustomOAuth2User details,
                                                        @RequestBody BabySettingUpdateRequest request) {
    settingService.updateBabySetting(details.getProviderId(), request);
    return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
  }

  @GetMapping("/family")
  public ResponseEntity<BaseResponse<FamilySettingResponse>> getFamilySetting(@AuthenticationPrincipal CustomOAuth2User details) {
    FamilySettingResponse settingData = settingService.getFamilySetting(details.getProviderId());
    return ResponseEntity.ok(new BaseResponse<>(settingData));
  }
}
