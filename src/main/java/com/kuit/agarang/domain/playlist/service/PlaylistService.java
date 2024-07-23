package com.kuit.agarang.domain.playlist.service;

import com.kuit.agarang.domain.memory.model.dto.MemoryDTO;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.domain.playlist.model.dto.MusicUrlsResponse;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistDto;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistResponse;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistsResponse;
import com.kuit.agarang.domain.playlist.model.entity.MemoryPlaylist;
import com.kuit.agarang.domain.playlist.model.entity.Playlist;
import com.kuit.agarang.domain.playlist.repository.MemoryPlaylistRepository;
import com.kuit.agarang.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MemoryPlaylistRepository memoryPlaylistRepository;
    private final MemoryRepository memoryRepository;

    public PlaylistsResponse getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistDto> playlistDtos = playlists.stream()
                .map(playlist -> new PlaylistDto(playlist.getId(), playlist.getName(), playlist.getImageUrl()))
                .collect(Collectors.toList());

        return new PlaylistsResponse(playlistDtos);
    }

    public PlaylistResponse getPlaylistById(Long id) {
        // TODO : 예외처리 바꾸기 (AgarangException)
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        PlaylistDto playlistDto = new PlaylistDto(playlist.getId(), playlist.getName(), playlist.getImageUrl());

        return new PlaylistResponse(playlistDto);
    }

    public MusicUrlsResponse getMusicUrlsByPlaylistId(Long playlistId, Long memberId) {
        // TODO : 예외처리 필요
        List<MemoryPlaylist> memoryPlaylists = memoryPlaylistRepository.findByPlaylistId(playlistId);

        List<Long> memoryIds = memoryPlaylists.stream()
                .map(memoryPlaylist -> memoryPlaylist.getMemory().getId())
                .collect(Collectors.toList());

        List<Memory> memories = memoryRepository.findAllById(memoryIds);

        List<String> musicUrls= memories.stream()
                .filter(memory -> memory.getMember().getId().equals(memberId))
                .map(Memory::getMusicUrl)
                .collect(Collectors.toList());

        return new MusicUrlsResponse(musicUrls);
    }
}
