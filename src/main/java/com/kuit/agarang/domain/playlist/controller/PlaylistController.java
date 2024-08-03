package com.kuit.agarang.domain.playlist.controller;

import com.kuit.agarang.domain.playlist.model.dto.DeleteMusicRequest;
import com.kuit.agarang.domain.playlist.model.dto.MusicBookmarkRequest;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistTracksResponse;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistsResponse;
import com.kuit.agarang.domain.playlist.service.PlaylistService;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<BaseResponse<PlaylistsResponse>> getAllPlaylists(@RequestParam Long memberId) {
        PlaylistsResponse playlistsResponse = playlistService.getAllPlaylists(memberId);
        return ResponseEntity.ok(new BaseResponse<>(playlistsResponse));
    }

    @GetMapping("/{id}/track")
    public ResponseEntity<BaseResponse<PlaylistTracksResponse>> getPlaylistTracks(@PathVariable Long id,
                                                                                  @RequestParam Long memberId) {
        PlaylistTracksResponse playlistTracksResponse = playlistService.getPlaylistTracks(id, memberId);
        return ResponseEntity.ok(new BaseResponse<>(playlistTracksResponse));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<BaseResponse<Void>> updateMusicBookmark(@RequestBody MusicBookmarkRequest musicBookmarkRequest) {
        playlistService.updateMusicBookmark(musicBookmarkRequest);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    @DeleteMapping("/music/delete")
    public ResponseEntity<BaseResponse<Void>> deleteMusic(@RequestBody DeleteMusicRequest deleteMusicRequest) {
        playlistService.deleteMusic(deleteMusicRequest);
        return ResponseEntity.ok(new BaseResponse<>());
    }
}
