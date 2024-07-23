package com.kuit.agarang.domain.playlist.controller;

import com.kuit.agarang.domain.playlist.model.dto.MusicUrlsResponse;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistDto;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistResponse;
import com.kuit.agarang.domain.playlist.model.dto.PlaylistsResponse;
import com.kuit.agarang.domain.playlist.service.PlaylistService;
import com.kuit.agarang.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    /*
     *   TODO : ResponseEntity 사용
     *   TODO :
     */
    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllPlaylists() {
        PlaylistsResponse playlistsResponse = playlistService.getAllPlaylists();
        return ResponseEntity.ok(new BaseResponse<>(playlistsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPlaylistById(@PathVariable Long id) {
        PlaylistResponse playlistResponse = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(new BaseResponse<>(playlistResponse));
    }

    @GetMapping("/{id}/music")
    public ResponseEntity<BaseResponse> getMusicUrlsByPlaylistId(@PathVariable Long id, @RequestParam Long memberId) {
        MusicUrlsResponse musicUrlsResponse = playlistService.getMusicUrlsByPlaylistId(id, memberId);
        return ResponseEntity.ok(new BaseResponse<>(musicUrlsResponse));
    }
}
