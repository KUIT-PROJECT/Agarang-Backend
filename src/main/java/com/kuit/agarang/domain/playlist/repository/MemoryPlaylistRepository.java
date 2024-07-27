package com.kuit.agarang.domain.playlist.repository;

import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.playlist.model.entity.MemoryPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryPlaylistRepository extends JpaRepository<MemoryPlaylist, Long> {
  void deleteByMemory(Memory memory);
}
