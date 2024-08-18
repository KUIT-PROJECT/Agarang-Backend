package com.kuit.agarang.domain.playlist.util;

import com.kuit.agarang.domain.memory.enums.Genre;
import com.kuit.agarang.domain.memory.enums.Instrument;
import com.kuit.agarang.domain.memory.enums.Mood;
import com.kuit.agarang.domain.playlist.model.entity.Playlist;
import com.kuit.agarang.domain.playlist.repository.PlaylistRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MusicSaveUtil {

    private final PlaylistRepository playlistRepository;

    public List<Playlist> getPlaylistIds(Genre genre, Mood mood, Instrument instrument) {
        List<Playlist> playlists = new ArrayList<>();

        //1. 전체 플레이리스트
        playlists.add(getPlaylist(1L));

        // 3. 태동이 느껴질 때 듣는
        if( mood == Mood.LOVELY || mood == Mood.BEAUTIFUL ||
                genre == Genre.BALLAD || genre == Genre.ACOUSTIC ||
                instrument == Instrument.HARP || instrument == Instrument.VIOLIN || instrument == Instrument.PIANO){
            playlists.add(getPlaylist(3L));
        }

        // 4. 하루를 정리하며 듣는
        if (mood == Mood.BRIGHT || mood == Mood.PEACEFUL ||
                genre == Genre.POP || genre == Genre.INDIE ||
                instrument == Instrument.XYLOPHONE || instrument == Instrument.TRUMPET || instrument == Instrument.FLUTE) {
            playlists.add(getPlaylist(4L));
        }

        // 5. 아침을 시작하며 듣는
        if (mood == Mood.HAPPY || mood == Mood.WARM ||
                genre == Genre.JAZZ || genre == Genre.ACOUSTIC ||
                instrument == Instrument.SAXOPHONE || instrument == Instrument.PIANO || instrument == Instrument.FLUTE) {
            playlists.add(getPlaylist(5L));
        }

        // 6. 운동하며 듣는
        if (mood == Mood.ENERGETIC ||
                genre == Genre.ROCK || genre == Genre.ELECTRONIC || genre == Genre.HIPHOP ||
                instrument == Instrument.ELECTRIC_GUITAR || instrument == Instrument.DRUM || instrument == Instrument.TRUMPET) {
            playlists.add(getPlaylist(6L));
        }

        // 7. 마음을 편안하게 하는
        if (mood == Mood.FANTASTIC || mood == Mood.PEACEFUL ||
                genre == Genre.ACOUSTIC || genre == Genre.INDIE ||
                instrument == Instrument.FLUTE || instrument == Instrument.XYLOPHONE || instrument == Instrument.VIOLIN) {
            playlists.add(getPlaylist(7L));
        }

        // 8. 혼자만의 시간을 가지며 듣는
        if (mood == Mood.TOUCHING ||
                genre == Genre.RNB || genre == Genre.BALLAD ||
                instrument == Instrument.CELLO || instrument == Instrument.PIANO || instrument == Instrument.HARP) {
            playlists.add(getPlaylist(8L));
        }

        return playlists;
    }

    private Playlist getPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.INVALID_PLAYLIST_ID));
    }
}
