package com.kuit.agarang.global.deploy.controller;

import com.kuit.agarang.global.deploy.model.dto.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @Value("${deploy.env}")
  private String env;
  @Value("${server.port}")
  private String serverPort;
  @Value("${deploy.address}")
  private String serverAddress;
  @Value("${deploy.name}")
  private String serverName;

  @GetMapping("/hc")
  public ResponseEntity<ServerInfo> healthCheck() {
    return ResponseEntity.ok(ServerInfo.builder()
      .port(serverPort)
      .address(serverAddress)
      .name(serverName)
      .build());
  }

  @GetMapping("/env")
  public ResponseEntity<String> getEnv() {
    return ResponseEntity.ok(env);
  }
}
