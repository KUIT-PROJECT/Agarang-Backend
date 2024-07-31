package com.kuit.agarang.domain.ai.model.dto;

import com.kuit.agarang.domain.ai.model.dto.MusicAnswer.MusicChoice;
import com.kuit.agarang.domain.memory.enums.Genre;
import com.kuit.agarang.domain.memory.enums.Instrument;
import com.kuit.agarang.domain.memory.enums.Mood;
import com.kuit.agarang.domain.memory.enums.Tempo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MusicInfo {
  private Instrument instrument;
  private Genre genre;
  private Mood mood;
  private Tempo tempo;

  @Builder
  public MusicInfo(Instrument instrument, Genre genre, Mood mood, Tempo tempo) {
    this.instrument = instrument;
    this.genre = genre;
    this.mood = mood;
    this.tempo = tempo;
  }

  public static MusicInfo from(MusicChoice musicChoice) {
    try {
      return MusicInfo.builder()
        .instrument(Instrument.valueOf(musicChoice.getInstrument()))
        .genre(Genre.valueOf(musicChoice.getGenre()))
        .mood(Mood.valueOf(musicChoice.getMood()))
        .tempo(Tempo.valueOf(musicChoice.getTempo()))
        .build();
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new RuntimeException("");
    }
  }
}
