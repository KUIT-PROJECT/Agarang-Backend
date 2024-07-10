package com.kuit.agarang.domain.member.model.entity;

import com.kuit.agarang.common.model.entity.BaseEntity;
import com.kuit.agarang.domain.baby.model.entity.Baby;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Baby baby;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private RefreshToken refreshToken;

  private String role;
}
