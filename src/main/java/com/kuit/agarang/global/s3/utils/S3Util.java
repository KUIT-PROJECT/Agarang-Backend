package com.kuit.agarang.global.s3.utils;

import com.kuit.agarang.global.s3.model.dto.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Util {

  private final S3FileUtil s3FileUtil;
  private final S3Client s3Client;
  @Value("${aws.s3.bucket}")
  private String bucket;

  public S3File upload(MultipartFile file) throws Exception {
    log.info("S3 파일 업로드가 시작되었습니다. [{} of {}]", file.getOriginalFilename(), file.getContentType());
    S3File s3File = s3FileUtil.convert(file)
      .orElseThrow(() -> new RuntimeException("파일 변환에 실패했습니다."));
    s3FileUtil.uploadTempFile(s3File);
    return upload(s3File);
  }

  private S3File upload(S3File s3File) throws Exception {
    try {
      PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(s3File.getFilename())
        .contentType(s3File.getContentType().getMimeType())
        .contentLength(s3File.getContentLength())
        .build();

      s3Client.putObject(request, RequestBody.fromBytes(s3File.getBytes()));
      log.info("S3 파일 업로드가 완료되었습니다. [{}]", s3File.getFilename());
      return s3File;
    } catch (S3Exception e) {
      log.error("AWS S3 통신에 문제가 발생했습니다.");
      throw new RuntimeException(e.getMessage());
    } finally {
      s3FileUtil.deleteTempFile(s3File);
    }
  }
}
