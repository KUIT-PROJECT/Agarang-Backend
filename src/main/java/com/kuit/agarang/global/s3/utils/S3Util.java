package com.kuit.agarang.global.s3.utils;

import com.kuit.agarang.global.s3.model.enums.FileCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Util {

  private final S3Client s3Client;
  @Value("${aws.s3.bucket}")
  private String bucket;
  @Value("${aws.s3.upload.tmpPath}")
  private String tmpPath;

  public String upload(MultipartFile file, FileCategory category) throws IOException {
    log.info("S3 파일 업로드가 시작되었습니다. [{} of {}]", file.getOriginalFilename(), category);

    File convertedFile = convert(file, category)
      .orElseThrow(() -> new RuntimeException("파일 변환에 실패했습니다."));

    try {
      validateFileExtension(convertedFile);

      String fileName = createFileName(convertedFile, category);
      PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .contentType(file.getContentType())
        .contentLength(file.getSize())
        .build();

      s3Client.putObject(request, RequestBody.fromFile(convertedFile));
      log.info("S3 파일 업로드가 완료되었습니다. [{} of {}]", fileName, category);
      return fileName;
    } catch (S3Exception e) {
      log.error("AWS S3 통신에 문제가 발생했습니다.");
      throw new RuntimeException(e.getMessage());
    } finally {
      if (convertedFile.exists()) {
        deleteLocalFile(convertedFile);
      }
    }
  }

  private void deleteLocalFile(File localFile) {
    if (localFile.delete()) {
      log.info("임시 업로드 파일이 성공적으로 삭제되었습니다. [{}]", localFile.getName());
      return;
    }
    log.info("임시 업로드 파일 삭제를 실패했습니다.");
  }

  private Optional<File> convert(MultipartFile file, FileCategory category) throws IOException {
    File convertedFile = new File(tmpPath + category.getPath() + file.getOriginalFilename());
    if (convertedFile.createNewFile()) {
      try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
        fos.write(file.getBytes());
      }
      return Optional.of(convertedFile);
    }
    return Optional.empty();
  }

  private String createFileName(File file, FileCategory fileCategory) {
    return fileCategory.getPath() + UUID.randomUUID() + "_" + file.getName();
  }

  private void validateFileExtension(File file) {
    String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
    List<String> allowedExtensions = Arrays.asList("jpg", "png", "jpeg", "mp3");

    if (!allowedExtensions.contains(fileExtension)) {
      throw new RuntimeException("지원하지 않는 파일확장자입니다.");
    }
  }
}
