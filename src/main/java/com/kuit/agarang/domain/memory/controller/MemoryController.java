package com.kuit.agarang.domain.memory.controller;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import com.kuit.agarang.domain.memory.model.dto.BookmarkRequest;
import com.kuit.agarang.domain.memory.model.dto.DeleteMemoryRequest;
import com.kuit.agarang.domain.memory.model.dto.ModifyMemoryRequest;
import com.kuit.agarang.domain.memory.enums.ViewType;
import com.kuit.agarang.domain.memory.model.dto.*;
import com.kuit.agarang.domain.memory.service.MemoryService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/memory")
@RequiredArgsConstructor
public class MemoryController {
  private final MemoryService memoryService;

  @Operation(summary = "다이어리 조회 (카드, 즐겨찾기, 월, 일)")
  @GetMapping
  public ResponseEntity<BaseResponse> getMemoriesByViewType(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @ModelAttribute MemoryRequest memoryRequest) {
    String requestViewType = memoryRequest.getViewType();
    log.info("memoryRequest : " + memoryRequest);
    ViewType viewType = ViewType.findBy(requestViewType);

    if (viewType.equals(ViewType.CARD)) {
      CardMemoriesResponse cardMemoriesResponse = memoryService.findMemory(customOAuth2User.getMemberId(), memoryRequest);
      return ResponseEntity.ok(new BaseResponse<>(cardMemoriesResponse));
    }

    if (viewType.equals(ViewType.DAILY)) {
      //TODO : 페이징 처리
      DailyMemoriesResponse dailyMemoriesResponse = memoryService.findDailyMemories(customOAuth2User.getMemberId());
      return ResponseEntity.ok(new BaseResponse<>(dailyMemoriesResponse));
    }

    if (viewType.equals(ViewType.MONTHLY)) {
      //TODO : 페이징 처리
      MonthlyMemoryResponse monthlyMemoryResponse = memoryService.findAllMonthlyThumbnails(customOAuth2User.getMemberId());
      return ResponseEntity.ok(new BaseResponse<>(monthlyMemoryResponse));
    }

    if (viewType.equals(ViewType.BOOKMARK)) {
      FavoriteMemoriesResponse favoriteMemoriesResponse = memoryService.findFavoriteMemories(customOAuth2User.getMemberId());
      return ResponseEntity.ok(new BaseResponse(favoriteMemoriesResponse));
    }
    throw new RuntimeException();
  }

  @PostMapping("/bookmark")
  public ResponseEntity<BaseResponse> setBookmark(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody BookmarkRequest bookmarkRequest) {
    memoryService.updateBookmark(customOAuth2User.getMemberId(), bookmarkRequest);
    return ResponseEntity.ok(new BaseResponse());
  }

  @PutMapping
  public ResponseEntity<BaseResponse> modifyMemory(@RequestBody ModifyMemoryRequest modifyMemoryRequest) {
    memoryService.modifyMemory(modifyMemoryRequest);
    return ResponseEntity.ok(new BaseResponse());
  }

  @DeleteMapping
  public ResponseEntity<BaseResponse> deleteMemory(@RequestBody DeleteMemoryRequest deleteMemoryRequest) {
    memoryService.removeMemory(deleteMemoryRequest);
    return ResponseEntity.ok(new BaseResponse());
  }

  @GetMapping("/{memoryId}")
  public ResponseEntity<BaseResponse> getMemoryById(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("memoryId") Long memoryId) {
    MemoryDTO memoryDTO = memoryService.findMemoryById(customOAuth2User.getMemberId(), memoryId);
    return ResponseEntity.ok(new BaseResponse(memoryDTO));
  }
  }
