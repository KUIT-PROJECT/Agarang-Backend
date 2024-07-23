package com.kuit.agarang.domain.playlist.model.dto;

import lombok.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistDto {
    private Long id;
    private String name;
    private String imageUrl;

    public PlaylistDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}