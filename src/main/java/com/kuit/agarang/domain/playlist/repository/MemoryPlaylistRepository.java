package com.kuit.agarang.domain.playlist.repository;

import com.kuit.agarang.domain.playlist.model.entity.MemoryPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryPlaylistRepository extends JpaRepository<MemoryPlaylist, Long> {
    List<MemoryPlaylist> findByPlaylistId(Long playlistId);
}
