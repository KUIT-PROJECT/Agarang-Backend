package com.kuit.agarang.domain.memory.controller;

import com.kuit.agarang.domain.memory.enums.ViewType;
import com.kuit.agarang.domain.memory.model.dto.FavoriteMemoriesResponse;
import com.kuit.agarang.domain.memory.model.dto.MemoryRequest;
import com.kuit.agarang.domain.memory.model.dto.MonthlyMemoryResponse;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoryRequest;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoryResponse;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoriesResponse;
import com.kuit.agarang.domain.memory.service.MemoryService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/memory")
@RequiredArgsConstructor
public class MemoryController {
  private final MemoryService memoryService;

  @GetMapping
  public ResponseEntity<BaseResponse> getMemoriesByViewType(@ModelAttribute MemoryRequest memoryRequest) {
    String requestViewType = memoryRequest.getViewType();
    log.info("memoryRequest : " + memoryRequest);
    ViewType viewType = ViewType.findBy(requestViewType);

    if (viewType.equals(ViewType.CARD)) {
      DailyMemoryResponse dailyMemoryResponse = memoryService.findMemory(memoryRequest);
      return ResponseEntity.ok(new BaseResponse<>(dailyMemoryResponse));
    }

    if (viewType.equals(ViewType.DAILY)) {
      //TODO : 페이징 처리
      DailyMemoriesResponse dailyMemoriesResponse = memoryService.findDailyMemories();
      return ResponseEntity.ok(new BaseResponse<>(dailyMemoriesResponse));
    }

    if (viewType.equals(ViewType.MONTHLY)) {
      //TODO : 페이징 처리
      MonthlyMemoryResponse monthlyMemoryResponse = memoryService.findAllMonthlyThumbnails();
      return ResponseEntity.ok(new BaseResponse<>(monthlyMemoryResponse));
    }

    if (viewType.equals(ViewType.FAVORITE)) {
      FavoriteMemoriesResponse favoriteMemoriesResponse = memoryService.findFavoriteMemories();
      return ResponseEntity.ok(new BaseResponse(favoriteMemoriesResponse));
    }
    throw new RuntimeException();
  }
}
