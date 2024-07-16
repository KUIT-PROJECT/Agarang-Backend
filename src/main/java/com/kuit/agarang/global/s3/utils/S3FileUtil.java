package com.kuit.agarang.global.s3.utils;

import com.kuit.agarang.global.s3.model.dto.S3File;
import com.kuit.agarang.global.s3.model.enums.ContentType;
import com.kuit.agarang.global.s3.model.enums.FileCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class S3FileUtil {

  @Value("${aws.s3.upload.tmpPath}")
  private String tmpPath;

  public Optional<S3File> convert(MultipartFile file, FileCategory category) throws Exception {
    ContentType contentType = getContentType(file)
      .orElseThrow(() -> new RuntimeException("지원하지 않는 파일확장자입니다."));
    validateFileCategory(contentType, category);
    return Optional.of(S3File.builder()
      .fileName(createFileName(file, category))
      .contentType(contentType)
      .contentLength(file.getSize())
      .bytes(file.getBytes())
      .build());
  }

  public void uploadTempFile(S3File s3File) throws IOException {
    File file = new File(tmpPath + s3File.getFileName());
    if (file.createNewFile()) {
      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(s3File.getBytes());
      }
    }
  }

  public void deleteTempFile(S3File s3File) {
    File file = new File(tmpPath + s3File.getFileName());
    if (file.exists()) {
      if (file.delete()) {
        log.info("임시 업로드 파일이 성공적으로 삭제되었습니다. [{}]", file.getName());
        return;
      }
      log.info("임시 업로드 파일 삭제를 실패했습니다.");
    }
  }

  private void validateFileCategory(ContentType contentType, FileCategory category) {
    boolean isCategoryValid = (contentType == ContentType.MP3) == (category == FileCategory.MUSIC);
    if (!isCategoryValid) {
      throw new RuntimeException("카테고리 분류가 잘못되었습니다.");
    }
  }

  private Optional<ContentType> getContentType(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    return ContentType.of(extension);
  }

  private String createFileName(MultipartFile file, FileCategory fileCategory) {
    return fileCategory.getPath() + UUID.randomUUID() + "_" + file.getOriginalFilename();
  }
}
