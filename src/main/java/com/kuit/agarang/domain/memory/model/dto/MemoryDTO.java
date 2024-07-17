package com.kuit.agarang.domain.memory.model.dto;

import com.kuit.agarang.domain.memory.model.entity.Hashtag;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.global.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemoryDTO {
  private String writer;
  private String Date;
  private String content;
  private List<String> hashTags;
  private boolean isFavorite;

  @Builder
  public MemoryDTO(String content, String date, List<String> hashTags, boolean isFavorite, String writer) {
    this.content = content;
    Date = date;
    this.hashTags = hashTags;
    this.isFavorite = isFavorite;
    this.writer = writer;
  }

  public static MemoryDTO of(Memory memory, boolean isFavorite) {
    return MemoryDTO.builder()
            .writer(memory.getMember().getRole())
            .date(DateUtil.formatLocalDateTime(memory.getCreatedAt(), "yyyy. MM. dd"))
            .content(memory.getText())
            .hashTags(memory.getHashtag().stream()
                    .map(Hashtag::getName)
                    .toList())
            .isFavorite(isFavorite)
            .build();
  }
}
