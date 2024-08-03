package com.kuit.agarang.domain.memory.repository;

import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.model.entity.MusicBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicBookmarkRepository extends JpaRepository<MusicBookmark, Long> {
    MusicBookmark findByMemoryAndMember(Memory memory, Member member);
    boolean existsByMemoryIdAndMemberId(Long memoryId, Long memberId);
    void deleteByMemory(Memory memory);
}