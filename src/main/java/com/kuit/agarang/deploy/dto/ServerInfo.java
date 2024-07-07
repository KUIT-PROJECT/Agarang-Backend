package com.kuit.agarang.deploy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {
  private String port;
  private String address;
  private String name;
}
