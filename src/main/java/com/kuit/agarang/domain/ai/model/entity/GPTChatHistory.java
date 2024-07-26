package com.kuit.agarang.domain.ai.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GPTChatHistory extends BaseEntity {
  @Id
  private String id;
  @Lob
  @Column(columnDefinition = "TEXT")
  private String imageUrl;
  private String musicTitle;
  private String hashtags;
  @Lob
  @Column(columnDefinition = "TEXT")
  private String questionAudioUrl;
  @Lob
  @Column(columnDefinition = "TEXT")
  private String historyMessages;
}
