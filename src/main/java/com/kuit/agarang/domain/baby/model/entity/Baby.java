package com.kuit.agarang.domain.baby.model.entity;

import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Baby extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "character_id")
  private Character character;

  private String code;
  private String name;
  private LocalDate expectedDueAt; // 출산 예정일
  private Double weight;

  @Builder
  public Baby(String code, String name, LocalDate expectedDueAt, Double weight) {
    this.code = code;
    this.name = name;
    this.expectedDueAt = expectedDueAt;
    this.weight = weight;
  }

  public Baby(Long id, String code, String name, LocalDate expectedDueAt, Double weight) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.expectedDueAt = expectedDueAt;
    this.weight = weight;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setExpectedDueAt(LocalDate expectedDueAt) {
    this.expectedDueAt = expectedDueAt;
  }
}
