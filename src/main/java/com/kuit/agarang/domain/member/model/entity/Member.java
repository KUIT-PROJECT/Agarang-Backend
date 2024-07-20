package com.kuit.agarang.domain.member.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import com.kuit.agarang.domain.baby.model.entity.Baby;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "baby_id")
  private Baby baby;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "refresh_token_id")
  private RefreshToken refreshToken;

  private String role;

  @Builder
  public Member(String role) {
    this.role = role;
  }

  public Member(Long id) {
    this.id = id;
  }
}
