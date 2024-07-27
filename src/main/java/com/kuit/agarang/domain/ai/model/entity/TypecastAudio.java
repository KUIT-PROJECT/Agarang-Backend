package com.kuit.agarang.domain.ai.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TypecastAudio extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String audioDownloadUrl;

  public TypecastAudio(String audioDownloadUrl) {
    this.audioDownloadUrl = audioDownloadUrl;
  }
}
