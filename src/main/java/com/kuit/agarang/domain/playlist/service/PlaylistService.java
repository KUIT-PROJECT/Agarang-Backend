package com.kuit.agarang.domain.playlist.service;

import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import com.kuit.agarang.domain.memory.model.entity.MusicBookmark;
import com.kuit.agarang.domain.memory.repository.MemoryRepository;
import com.kuit.agarang.domain.playlist.model.dto.*;
import com.kuit.agarang.domain.playlist.model.entity.MemoryPlaylist;
import com.kuit.agarang.domain.playlist.model.entity.Playlist;
import com.kuit.agarang.domain.playlist.repository.MemoryPlaylistRepository;
import com.kuit.agarang.domain.memory.repository.MusicBookmarkRepository;
import com.kuit.agarang.domain.playlist.repository.PlaylistRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MemoryPlaylistRepository memoryPlaylistRepository;
    private final MemoryRepository memoryRepository;
    private final MusicBookmarkRepository musicBookmarkRepository;
    private final MemberRepository memberRepository;

    public PlaylistsResponse getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistDto> playlistDtos = playlists.stream()
                .map(PlaylistDto::of)
                .collect(Collectors.toList());

        return new PlaylistsResponse(playlistDtos);
    }

    public PlaylistTracksResponse getPlaylistTracks(Long playlistId, Long memberId) {
        // TODO : 인가처리에 따라 수정
        // TODO : 예외처리 수정
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("playlistId : " + playlistId + " 해당 플레이리스트가 존재하지 않습니다."));

        PlaylistDto playlistDto = PlaylistDto.of(playlist);

        List<MemoryPlaylist> memoryPlaylists = memoryPlaylistRepository.findByPlaylistId(playlistId);

        List<MusicDto> musicDtos = memoryPlaylists.stream()
                .map(memoryPlaylist -> {
                    Long memoryId = memoryPlaylist.getMemory().getId();
                    Memory memory = memoryRepository.findById(memoryId)
                            .orElseThrow(() -> new RuntimeException("member 접근 오류"));

                    boolean isBookmarked = checkBookmarkStatus(memoryId, memberId);

                    return MusicDto.of(memory, isBookmarked);
                })
                .collect(Collectors.toList());

        int totalTrackCount = musicDtos.size();
        int totalTrackTime = totalTrackCount * 40;

        return new PlaylistTracksResponse(playlistDto, musicDtos, totalTrackCount, totalTrackTime);
    }

    @Transactional
    public void updateMusicBookmark(MusicBookmarkRequest musicBookmarkRequest) {
        Memory memory = memoryRepository.findById(musicBookmarkRequest.getMemoryId())
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));

        Member member = memberRepository.findById(musicBookmarkRequest.getMemberId())
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMBER_ID));

        MusicBookmark musicBookmark = musicBookmarkRepository.findByMemory(memory);

        Playlist favoritePlaylist = playlistRepository.findById(2L)
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_PLAYLIST_ID));

        if (musicBookmark != null) {
            musicBookmarkRepository.delete(musicBookmark);

            MemoryPlaylist memoryPlaylist = memoryPlaylistRepository.findByMemoryAndPlaylist(memory,favoritePlaylist);
            memoryPlaylistRepository.delete(memoryPlaylist);
        } else {
            musicBookmarkRepository.save(new MusicBookmark(member, memory));
            memoryPlaylistRepository.save(new MemoryPlaylist(memory,favoritePlaylist));
        }
    }

    @Transactional
    public void deleteMusic(DeleteMusicRequest deleteMusicRequest) {
        Memory memory = memoryRepository.findById(deleteMusicRequest.getMemoryId())
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_MEMORY_ID));
        //TODO: 예외처리
        Playlist playlist = playlistRepository.findById(deleteMusicRequest.getPlaylistId())
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_PLAYLIST_ID));

        memoryPlaylistRepository.deleteByMemoryAndPlaylist(memory, playlist);
        musicBookmarkRepository.deleteByMemory(memory);
    }

    private boolean checkBookmarkStatus(Long memoryId, Long memberId) {
        return musicBookmarkRepository.existsByMemoryIdAndMemberId(memoryId, memberId);
    }
}
