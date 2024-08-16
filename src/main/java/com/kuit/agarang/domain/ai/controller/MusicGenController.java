package com.kuit.agarang.domain.ai.controller;

import com.kuit.agarang.domain.ai.model.dto.musicGen.MusicGenResponse;
import com.kuit.agarang.domain.ai.service.MusicGenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/music-gen")
public class MusicGenController {

  private final MusicGenService musicGenService;

  @PostMapping("/webhook")
  public void webhook(@RequestBody MusicGenResponse response) {
    musicGenService.saveMusic(response);
  }
}
