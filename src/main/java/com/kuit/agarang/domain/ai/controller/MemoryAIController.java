package com.kuit.agarang.domain.ai.controller;

import com.kuit.agarang.domain.ai.model.dto.MusicAnswer;
import com.kuit.agarang.domain.ai.model.dto.TextAnswer;
import com.kuit.agarang.domain.ai.model.dto.QuestionResponse;
import com.kuit.agarang.domain.ai.service.MemoryAIService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @PostMapping("/first-ans")
  public ResponseEntity<BaseResponse<QuestionResponse>> getNextQuestion(@RequestBody TextAnswer answer) {
    return ResponseEntity.ok(new BaseResponse<>(memoryAIService.getNextQuestion(answer)));
  }

  @PostMapping("/second-ans")
  public ResponseEntity<BaseResponse<Void>> saveLastAnswer(@RequestBody TextAnswer answer) {
    memoryAIService.saveLastAnswer(answer);
    memoryAIService.createMemoryText(answer.getId());
    return ResponseEntity.ok(new BaseResponse<>());
  }

  @PostMapping("/music")
  public ResponseEntity<BaseResponse<Void>> saveLastAnswer(@RequestBody MusicAnswer answer) {
    memoryAIService.saveMusicChoice(answer);
    return ResponseEntity.ok(new BaseResponse<>());
  }
}
