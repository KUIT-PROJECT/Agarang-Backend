package com.kuit.agarang.domain.home.controller;

import com.kuit.agarang.domain.home.model.dto.CharacterChangeRequest;
import com.kuit.agarang.domain.home.model.dto.CharacterSettingResponse;
import com.kuit.agarang.domain.home.service.CharacterService;
import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home/setting/character")
@RequiredArgsConstructor
public class CharacterController {

  private final CharacterService characterService;

  @GetMapping
  public ResponseEntity<BaseResponse<List<CharacterSettingResponse>>> getCharactersByDate(@AuthenticationPrincipal CustomOAuth2User details) {
    List<CharacterSettingResponse> charactersByDate = characterService.getCharactersByDate(details.getMemberId());
    return ResponseEntity.ok(new BaseResponse<>(charactersByDate));

  }

  @PatchMapping
  public ResponseEntity<BaseResponse<Void>> updateBabyCharacter(@AuthenticationPrincipal CustomOAuth2User details,
                                                                @RequestBody CharacterChangeRequest request) {
    characterService.updateCharacterSetting(details.getMemberId(), request.getCharacterId());
    return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
  }
}
