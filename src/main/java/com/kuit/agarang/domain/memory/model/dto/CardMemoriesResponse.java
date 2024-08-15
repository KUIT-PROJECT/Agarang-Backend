package com.kuit.agarang.domain.memory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardMemoriesResponse {
  List<String> thumbNails;
  List<MemoryDTO> memories;
}
