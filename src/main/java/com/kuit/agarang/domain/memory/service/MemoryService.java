package com.kuit.agarang.domain.memory.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoryDTO;
import com.kuit.agarang.domain.memory.model.dto.FavoriteMemoriesResponse;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoryResponse;
import com.kuit.agarang.domain.memory.model.dto.DailyMemoriesResponse;
import com.kuit.agarang.domain.memory.model.dto.MemoryDTO;
import com.kuit.agarang.domain.memory.model.dto.MemoryImageDTO;
import com.kuit.agarang.domain.memory.model.dto.MemoryRequest;
import com.kuit.agarang.domain.memory.model.dto.MonthlyMemoryDTO;
import com.kuit.agarang.domain.memory.model.dto.MonthlyMemoryResponse;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.repository.MemoryBookmarkRepository;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.global.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoryService {
  private final MemoryRepository memoryRepository;
  private final MemoryBookmarkRepository memoryBookmarkRepository;

  public DailyMemoryResponse findMemory(MemoryRequest memoryRequest) {
    //TODO : 회원 JWT -> 아기 조회 로직 추가 필요
    Baby baby = new Baby(1L, "DXW1234", "아가", LocalDate.of(2025, 1, 1), 1.8D);

    String date = memoryRequest.getDate();
    LocalDate selectedDate = DateUtil.convertStringToLocalDate(date);

    List<Object[]> memoriesByDateAndBaby = memoryRepository.findByMemoriesByDateAndBabyOrdeOrderByCreatedAtDesc(selectedDate, baby);
    log.info("memoriesByDateAndBaby: " + memoriesByDateAndBaby);
    List<MemoryDTO> memoryDTOS = memoriesByDateAndBaby.stream()
            .map(result -> MemoryDTO.of((Memory) result[0], (boolean) result[1]))
            .toList();
    LocalDate startDate = DateUtil.findDate(selectedDate, -3);
    LocalDate endDate = DateUtil.findDate(selectedDate, 3);
    List<String> imageUrlsByDate = memoryRepository.findImageUrlsByDate(startDate, endDate);
    return new DailyMemoryResponse(imageUrlsByDate, memoryDTOS);
  }

  public DailyMemoriesResponse findDailyMemories() {
    //TODO : 회원 JWT -> 아기 조회 로직 추가 필요
    Baby baby = new Baby(1L,"DXW1234", "아가", LocalDate.of(2025, 1, 1), 1.8D);

    List<Memory> memories = memoryRepository.findByBabyOrderByCreatedAtDesc(baby);
    List<DailyMemoryDTO> dailyMemoryDTOS = memories.stream()
            .map(memory -> DailyMemoryDTO.from(memory))
            .toList();
    return new DailyMemoriesResponse(dailyMemoryDTOS);
  }

  public MonthlyMemoryResponse findAllMonthlyThumbnails() {
    //TODO : 회원 JWT -> 아기 조회 로직 추가 필요
    Baby baby = new Baby(1L, "DXW1234", "아가", LocalDate.of(2025, 1, 1), 1.8D);
    List<Memory> allMemories = memoryRepository.findByBaby(baby);

    Map<String, List<Memory>> memoriesByMonth = allMemories.stream()
            .collect(Collectors.
                    groupingBy(memory -> memory.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM"))));

    List<MonthlyMemoryDTO> monthlyMemories = new ArrayList<>();
    memoriesByMonth.forEach((month, memories) -> {
      Memory latestMemory = memories.stream()
              .max(Comparator.comparing(Memory::getCreatedAt))
              .orElse(null);
      if (latestMemory != null) {
        monthlyMemories.add(new MonthlyMemoryDTO(month, latestMemory.getImageUrl()));
      }
    });

    return new MonthlyMemoryResponse(monthlyMemories);
  }

  public FavoriteMemoriesResponse findFavoriteMemories() {
    // TODO : 회원 JWT -> Member 조회 및 예외처리 필요
    Member member = new Member(1L);
    List<Memory> favoriteMemoriesByMember = memoryBookmarkRepository.findMemoryBookmarksByMember(member);
    List<MemoryImageDTO> memoryImageDTOS = favoriteMemoriesByMember.stream()
            .map(memory -> MemoryImageDTO.of(memory.getId(), memory.getImageUrl()))
            .toList();
    return new FavoriteMemoriesResponse(memoryImageDTOS);
  }
}
