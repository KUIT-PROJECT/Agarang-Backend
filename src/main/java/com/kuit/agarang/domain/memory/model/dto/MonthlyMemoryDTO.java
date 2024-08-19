package com.kuit.agarang.domain.memory.model.dto;

import com.kuit.agarang.domain.memory.model.entity.Memory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MonthlyMemoryDTO {
  private long id;
  private String date;
  private String imageUrl;

  private MonthlyMemoryDTO(long id, String date, String imageUrl) {
    this.id = id;
    this.date = date;
    this.imageUrl = imageUrl;
  }

  public static MonthlyMemoryDTO of(String date, Memory memory) {
    return new MonthlyMemoryDTO(memory.getId(), date, memory.getImageUrl());
  }
}
