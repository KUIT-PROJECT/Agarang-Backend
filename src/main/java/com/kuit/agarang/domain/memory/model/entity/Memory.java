package com.kuit.agarang.domain.memory.model.entity;


import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.memory.enums.Genre;
import com.kuit.agarang.domain.memory.enums.Instrument;
import com.kuit.agarang.domain.memory.enums.Mood;
import com.kuit.agarang.domain.memory.enums.Tempo;
import com.kuit.agarang.global.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "baby_id")
  private Baby baby;

  private String imageUrl;
  private String musicTitle;
  private String text;

  @Setter
  private String musicGenId;
  @Setter
  private String musicUrl;

  @Enumerated(EnumType.STRING)
  private Genre genre;

  @Enumerated(EnumType.STRING)
  private Mood mood;

  @Enumerated(EnumType.STRING)
  private Tempo tempo;

  @Enumerated(EnumType.STRING)
  private Instrument instrument;

  @Setter
  @OneToMany(mappedBy = "memory")
  private List<Hashtag> hashtags;

  @Builder
  public Memory(Long id, Member member, Baby baby, String imageUrl, String musicTitle, String musicUrl, String text, String musicGenId, Genre genre, Mood mood, Tempo tempo, Instrument instrument, List<Hashtag> hashtags) {
    this.id = id;
    this.member = member;
    this.baby = baby;
    this.imageUrl = imageUrl;
    this.musicTitle = musicTitle;
    this.musicUrl = musicUrl;
    this.text = text;
    this.musicGenId = musicGenId;
    this.genre = genre;
    this.mood = mood;
    this.tempo = tempo;
    this.instrument = instrument;
    this.hashtags = hashtags;
  }

  public void updateMemory(String text) {
    this.text = text;
  }
}
