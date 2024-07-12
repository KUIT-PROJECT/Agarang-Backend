package com.kuit.agarang.global.deploy.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerInfo {
  private String port;
  private String address;
  private String name;

  @Builder
  public ServerInfo(String port, String address, String name) {
    this.port = port;
    this.address = address;
    this.name = name;
  }
}
