package com.kuit.agarang.domain.memory.model.entity;


import com.kuit.agarang.common.model.entity.BaseEntity;
import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.member.model.entity.Member;
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
public class Memory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Baby baby;

  private String imageUrl;
  private String text;
  private String genre;
  private String mood;
  private String tempo;
  private String instrumentType;
}
