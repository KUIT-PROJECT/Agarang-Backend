package com.kuit.agarang.global.s3.model.dto;

import com.kuit.agarang.global.s3.model.enums.ContentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class S3File {
  private String fileName;
  private ContentType contentType;
  private Long contentLength;
  private byte[] bytes;

  @Builder
  public S3File(String fileName, ContentType contentType, Long contentLength, byte[] bytes) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.contentLength = contentLength;
    this.bytes = bytes;
  }
}
