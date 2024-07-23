package com.kuit.agarang.global.discord.model.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class HeaderInfo {

  private static final String QUERY_PREFIX = "?";

  private String host;
  private String cookie;
  private String method;
  private String url;
  private String queryString;

  @Builder
  public HeaderInfo(String host, String cookie, String method, String url, String queryString) {
    this.host = host;
    this.cookie = cookie;
    this.method = method;
    this.url = url;
    this.queryString = queryString;
  }

  public static HeaderInfo of(HttpServletRequest request) {
    return HeaderInfo.builder()
      .host(request.getHeader("host"))
      .cookie(request.getHeader("cookie"))
      .method(request.getMethod())
      .url(request.getRequestURL().toString())
      .queryString(request.getQueryString() == null ? null : QUERY_PREFIX + request.getQueryString())
      .build();
  }
}
