package com.kuit.agarang.domain.ai.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TypecastAudio extends BaseEntity {

  @Id
  private String id;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String audioDownloadUrl;
}
