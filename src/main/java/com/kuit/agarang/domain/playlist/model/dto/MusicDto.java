package com.kuit.agarang.domain.playlist.model.dto;

import com.kuit.agarang.domain.memory.model.entity.Memory;
import lombok.*;

@Getter
@NoArgsConstructor
public class MusicDto {
    private String imageUrl;
    private String musicTitle;
    private String musicUrl;
    private String mood;
    private boolean isBookmarked;

    @Builder
    public MusicDto(String imageUrl, String musicTitle, String musicUrl, String mood, boolean isBookmarked) {
        this.imageUrl = imageUrl;
        this.musicTitle = musicTitle;
        this.musicUrl = musicUrl;
        // TODO : mood -> hashtag 여러개로 수정
        this.mood = mood;
        this.isBookmarked = isBookmarked;
    }

    public static MusicDto of(Memory memory, boolean isBookmarked) {
        return MusicDto.builder()
                .imageUrl(memory.getImageUrl())
                .musicTitle(memory.getMusicTitle())
                .musicUrl(memory.getMusicUrl())
                .mood(memory.getMood().toString())
                .isBookmarked(isBookmarked)
                .build();
    }
}
