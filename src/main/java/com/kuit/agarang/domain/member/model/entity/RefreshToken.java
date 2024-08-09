package com.kuit.agarang.domain.member.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String value;

  @Builder
  public RefreshToken(String value) {
    this.value = value;
  }

  public static RefreshToken of(String value) {
    return RefreshToken.builder()
        .value(value)
        .build();
  }

  public void changeValue(String value) {
    this.value = value;
  }
}
