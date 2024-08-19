package com.kuit.agarang.domain.memory.model.dto;

import com.kuit.agarang.domain.memory.model.entity.Hashtag;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.global.common.utils.DateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemoryDTO {
  private long id;
  private String writer;
  private String date;
  private String content;
  private String musicUrl;
  private String imageUrl;
  private List<String> hashTags;
  private boolean isBookmarked;

  @Builder
  public MemoryDTO(long id, String writer, String date, String content, String musicUrl, String imageUrl, List<String> hashTags, boolean isBookmarked) {
    this.id = id;
    this.writer = writer;
    this.date = date;
    this.content = content;
    this.musicUrl = musicUrl;
    this.imageUrl = imageUrl;
    this.hashTags = hashTags;
    this.isBookmarked = isBookmarked;
  }

  public static MemoryDTO of(Memory memory, boolean isBookmarked) {
    return MemoryDTO.builder()
            .id(memory.getId())
            .writer(memory.getMember().getRole())
            .date(DateUtil.formatLocalDateTime(memory.getCreatedAt(), "yyyy. MM. dd"))
            .content(memory.getText())
            .musicUrl(memory.getMusicUrl())
            .hashTags(memory.getHashtags().stream()
                    .map(Hashtag::getName)
                    .toList())
            .imageUrl(memory.getImageUrl())
            .isBookmarked(isBookmarked)
            .build();
  }
}
