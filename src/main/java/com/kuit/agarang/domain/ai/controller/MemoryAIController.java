package com.kuit.agarang.domain.ai.controller;

import com.kuit.agarang.domain.ai.model.dto.QuestionResponse;
import com.kuit.agarang.domain.ai.service.MemoryAIService;
import com.kuit.agarang.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/memory/ai")
@RequiredArgsConstructor
public class MemoryAIController {

  private final MemoryAIService memoryAIService;

  @PostMapping("/image-to-question")
  public ResponseEntity<BaseResponse<QuestionResponse>> getImageQuestion(@RequestParam MultipartFile image) throws Exception {
    return ResponseEntity.ok(new BaseResponse<>(memoryAIService.getFirstQuestion(image)));
  }
}
