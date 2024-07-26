package com.kuit.agarang.domain.playlist.repository;

import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.playlist.model.entity.MusicBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicBookmarkRepository extends JpaRepository<MusicBookmark, Long> {
    MusicBookmark findByMemoryAndMemberId(Memory memory, Long memberId);
}