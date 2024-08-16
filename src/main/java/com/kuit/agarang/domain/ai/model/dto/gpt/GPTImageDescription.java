package com.kuit.agarang.domain.ai.model.dto.gpt;

import com.kuit.agarang.domain.memory.model.entity.Hashtag;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GPTImageDescription {
  private String text;
  private List<String> noun;

  public List<Hashtag> convertNoun(Memory memory) {
    return this.noun.stream().map(x ->
      Hashtag.builder().memory(memory).name(x).build()).collect(Collectors.toList());
  }
}
