package com.kuit.agarang.global.s3.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public enum ContentType {

  JPG("image/jpeg", "jpg"),
  JPEG("image/jpeg", "jpeg"),
  PNG("image/png", "png"),
  MP3("audio/mpeg", "mp3");

  private final String mimeType;
  private final String extension;
  private static final Map<String, ContentType> allowedExtensions;

  static {
    allowedExtensions = new HashMap<>();
    for (ContentType c : ContentType.values()) {
      allowedExtensions.put(c.getExtension(), c);
    }
  }

  public static Optional<ContentType> of(String extension) {
    return Optional.ofNullable(allowedExtensions.get(extension));
  }
}
