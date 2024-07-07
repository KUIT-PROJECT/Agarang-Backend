package com.kuit.agarang.deploy.controller;

import com.kuit.agarang.deploy.dto.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @Value("${server.env}")
  private String env;
  @Value("${server.port}")
  private String serverPort;
  @Value("${serverAddress}")
  private String serverAddress;
  @Value("${serverName}")
  private String serverName;

  // TODO: "/hc", "/env" -> security permit-all

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
