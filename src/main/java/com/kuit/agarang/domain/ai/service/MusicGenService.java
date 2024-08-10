package com.kuit.agarang.domain.ai.service;

import com.kuit.agarang.domain.ai.model.dto.musicGen.MusicGenRequest;
import com.kuit.agarang.domain.ai.model.dto.musicGen.MusicGenResponse;
import com.kuit.agarang.domain.ai.utils.MusicGenClientUtil;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.exception.exception.OpenAPIException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.model.enums.ContentType;
import com.kuit.agarang.global.s3.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicGenService {

  @Value("${app.baseUrl}")
  private String baseUrl;
  @Value("${music-gen.version}")
  private String version;
  private final MusicGenClientUtil musicGenClientUtil;
  private final S3Util s3Util;
  private final MemoryRepository memoryRepository;

  private static final String WEBHOOK_URI = "/api/ai/music-gen/webhook";
  private static final Integer MUSIC_DURATION = 5;

  public String getMusic(String prompt) {
    MusicGenResponse response
      = musicGenClientUtil.post(
      new MusicGenRequest(version, baseUrl + WEBHOOK_URI, prompt, MUSIC_DURATION), MusicGenResponse.class);
    log.info("created musicgen id : {}", response.getId());
    return response.getId();
  }

  public void saveMusic(MusicGenResponse response) {
    if ("failed".equals(response.getStatus())) {
      log.info("musicgen error {}", response.getError());
      throw new OpenAPIException(BaseResponseStatus.FAIL_CREATE_MUSIC);
    }
    log.info("webhook musicgen id : {}", response.getId());
    log.info("webhook musicgen started at : {}", response.getStartedAt());
    log.info("webhook musicgen completed at : {}", response.getCompletedAt());

    S3File s3File = null;
    try {
      Path directoryPath = Paths.get("./temp/audio");
      if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
      }

      String filename = "music_gen_" + System.currentTimeMillis() + ".mp3";
      Path tempFilePath = directoryPath.resolve(filename);

      URL url = new URL(response.getOutput());
      Files.copy(url.openStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
      log.info("파일 다운로드 완료: {}", tempFilePath.toString());

      s3File = S3File.builder()
        .filename(ContentType.MP3.getPath() + filename)
        .contentType(ContentType.MP3)
        .contentLength(Files.size(tempFilePath))
        .bytes(Files.readAllBytes(tempFilePath))
        .build();
      s3File = s3Util.upload(s3File);
    } catch (IOException e) {
      throw new BusinessException(BaseResponseStatus.FILE_DOWNLOAD_ERROR);
    }

    Optional<Memory> optionalMemory = memoryRepository.findByMusicGenId(response.getId());
    if (optionalMemory.isEmpty()) {
      log.info("memory empty and musicgen output url : {}", response.getOutput());
      log.error("music gen webhook error : not found member by musicgenId");
      return;
    }
    Memory memory = optionalMemory.get();
    memory.setMusicUrl(s3File.getObjectUrl());
    memoryRepository.save(memory);
  }
}
