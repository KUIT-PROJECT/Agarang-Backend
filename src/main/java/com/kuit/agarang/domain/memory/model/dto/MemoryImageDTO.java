package com.kuit.agarang.domain.memory.model.dto;

import com.kuit.agarang.domain.memory.model.entity.Memory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryImageDTO {
  private long id;
  private String imageUrl;

  @Builder
  public MemoryImageDTO(long id, String imageUrl) {
    this.id = id;
    this.imageUrl = imageUrl;
  }

  public static MemoryImageDTO of(long id, String imageUrl) {
    return MemoryImageDTO.builder()
            .id(id)
            .imageUrl(imageUrl)
            .build();
  }

  public static MemoryImageDTO from(Memory memory) {
    return MemoryImageDTO.of(memory.getId(), memory.getImageUrl());
  }
}
