package com.kuit.agarang.domain.memory.service;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.baby.repository.BabyRepository;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.memory.model.dto.*;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.model.entity.MemoryBookmark;
import com.kuit.agarang.domain.memory.repository.HashTagRepository;
import com.kuit.agarang.domain.memory.repository.MemoryBookmarkRepository;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.domain.memory.repository.MusicBookmarkRepository;
import com.kuit.agarang.domain.playlist.repository.MemoryPlaylistRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoryService {
  private final MemoryRepository memoryRepository;
  private final MemoryBookmarkRepository memoryBookmarkRepository;
  private final MusicBookmarkRepository musicBookmarkRepository;
  private final MemoryPlaylistRepository memoryPlaylistRepository;
  private final HashTagRepository hashTagRepository;
  private final MemberRepository memberRepository;

  public CardMemoriesResponse findMemory(Long memberId, MemoryRequest memoryRequest) {
    String date = memoryRequest.getDate();
    LocalDate selectedDate = DateUtil.convertStringToLocalDate(date);

    Member member = memberRepository.findByIdFetchJoinBaby(memberId)
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));

    List<MemoryDTO> memoryDTOS = getMemoriesByDateAndBaby(member, selectedDate);
    List<String> imageUrlsByDate = getImageThumbnails(selectedDate);

    return new CardMemoriesResponse(imageUrlsByDate, memoryDTOS);
  }

  private List<MemoryDTO> getMemoriesByDateAndBaby(Member member, LocalDate selectedDate) {


    List<MemoryBookmarkedDTO> memoriesByDateAndBaby = memoryRepository.findByDateAndBabyForMemoryCard(selectedDate, member.getBaby());
    return memoriesByDateAndBaby.stream()
            .map(result -> MemoryDTO.of(result.getMemory(), result.isBookmarked()))
            .toList();
  }

  private List<String> getImageThumbnails(LocalDate today) {
    final int beforeRange = -3;
    final int afterRange = 3;

    LocalDate startDate = DateUtil.findDate(today, beforeRange);
    LocalDate endDate = DateUtil.findDate(today, afterRange);

    List<Memory> memoriesInDates = memoryRepository.findMemoriesInDates(startDate, endDate);

    List<String> thumbnails = Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(7) // 7일간의 날짜 스트림 생성 (이전 3일, 오늘, 이후 3일)
            .filter(date -> !date.equals(today)) // 오늘 날짜는 제외
            .map(date -> memoriesInDates.stream()
                    .filter(memory -> memory.getCreatedAt().toLocalDate().equals(date))
                    .findFirst()
                    .map(Memory::getImageUrl)
                    .orElse("")) // 해당 날짜에 메모리가 없으면 빈 문자열을 반환
            .collect(Collectors.toList());
    return thumbnails;
  }

  public DailyMemoriesResponse findDailyMemories(Long memberId) {
    Member member = memberRepository.findByIdFetchJoinBaby(memberId)
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));

    List<Memory> memories = memoryRepository.findByBabyOrderByCreatedAtDesc(member.getBaby());
    List<DailyMemoryDTO> dailyMemoryDTOS = memories.stream()
            .map(memory -> DailyMemoryDTO.from(memory))
            .toList();
    return new DailyMemoriesResponse(dailyMemoryDTOS);
  }

  public MonthlyMemoryResponse findAllMonthlyThumbnails(Long memberId) {
    Member member = memberRepository.findByIdFetchJoinBaby(memberId)
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));
    List<Memory> allMemories = memoryRepository.findByBaby(member.getBaby());

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
        monthlyMemories.add(MonthlyMemoryDTO.of(month, latestMemory.getImageUrl()));
      }
    });

    return new MonthlyMemoryResponse(monthlyMemories);
  }

  public FavoriteMemoriesResponse findFavoriteMemories(Long memberId) {
    List<Memory> favoriteMemoriesByMember = memoryBookmarkRepository.findMemoryBookmarksByMember(memberId);
    List<MemoryImageDTO> memoryImageDTOS = favoriteMemoriesByMember.stream()
            .map(memory -> MemoryImageDTO.of(memory.getId(), memory.getImageUrl()))
            .toList();
    return new FavoriteMemoriesResponse(memoryImageDTOS);
  }

  @Transactional
  public void updateBookmark(Long memberId, BookmarkRequest bookmarkRequest) {
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.NOT_FOUND_MEMBER));
    Memory memory = memoryRepository.findById(bookmarkRequest.getMemoryId())
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));

    Optional<MemoryBookmark> memoryBookmark = memoryBookmarkRepository.findByMemoryAndMemberId(memory, 1L);

    if(memoryBookmark.isPresent()) {
      memoryBookmarkRepository.delete(memoryBookmark.get());
      return;
    }
    memoryBookmarkRepository.save(new MemoryBookmark(member, memory));
  }

  @Transactional
  public void modifyMemory(ModifyMemoryRequest modifyMemoryRequest) {
    Memory memory = memoryRepository.findById(modifyMemoryRequest.getMemoryId())
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));
    memory.updateMemory(modifyMemoryRequest.getText());
  }

  @Transactional
  public void removeMemory(DeleteMemoryRequest deleteMemoryRequest) {
    Memory memory = memoryRepository.findById(deleteMemoryRequest.getMemoryId())
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));

    memoryBookmarkRepository.deleteByMemory(memory);
    musicBookmarkRepository.deleteByMemory(memory);
    memoryPlaylistRepository.deleteByMemory(memory);
    hashTagRepository.deleteByMemory(memory);
    memoryRepository.delete(memory);
  }

  public MemoryDTO findMemoryById(Long memberId, Long memoryId) {
    Memory memory = memoryRepository.findByIdAndMemberId(memoryId, memberId)
            .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));
    Optional<MemoryBookmark> memoryBookmark = memoryBookmarkRepository.findByMemoryAndMemberId(memory, 1L);
    boolean isBookmarked = memoryBookmark.isPresent();
    return MemoryDTO.of(memory, isBookmarked);
  }
}
