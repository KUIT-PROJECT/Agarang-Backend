package com.kuit.agarang.domain.memory.controller;

import com.kuit.agarang.domain.memory.dto.MessageRequest;
import com.kuit.agarang.domain.memory.service.TypecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai.tts")
@RequiredArgsConstructor
public class TypecastController {

  private final TypecastService typecastService;

  @PostMapping("/character/bubble")
  public ResponseEntity<String> tts(@RequestBody MessageRequest request) throws InterruptedException {
    // TODO : 응답 형식 확정 후 수정하기
    return ResponseEntity.ok(typecastService.getAudioDownloadUrl(request));
  }
}
