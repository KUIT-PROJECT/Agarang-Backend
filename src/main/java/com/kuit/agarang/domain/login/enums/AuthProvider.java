package com.kuit.agarang.domain.login.enums;

import lombok.Getter;

@Getter
public enum AuthProvider {

  KAKAO("kakao_"),
  GOOGLE("google_");

  private final String prefix;

  AuthProvider(String prefix) {
    this.prefix = prefix;
  }

}