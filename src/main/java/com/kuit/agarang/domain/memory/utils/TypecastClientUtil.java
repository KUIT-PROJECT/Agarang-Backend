package com.kuit.agarang.domain.memory.utils;

import com.kuit.agarang.domain.memory.config.WebClientConfig;
import com.kuit.agarang.domain.memory.dto.TypecastRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TypecastClientUtil {

  private final WebClientConfig webClientConfig;
  @Value("${typecast.baseUrl}")
  private String baseUrl;
  @Value("${typecast.apiKey}")
  private String apiKey;

  public <T> T post(String uri, TypecastRequest requestDto, Class<T> responseClass) {
    return webClientConfig.webClient().post()
      .uri(baseUrl + uri)
      .header("Authorization", "Bearer " + apiKey)
      .bodyValue(requestDto)
      .retrieve()
      // TODO : Exception 처리 추가 후 수정하기
      .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
      .bodyToMono(responseClass)
      .block();
  }

  public <T> T get(String uri, Class<T> responseClass) {
    return webClientConfig.webClient().get()
      .uri(uri)
      .header("Authorization", "Bearer " + apiKey)
      .retrieve()
      // TODO : Exception 처리 추가 후 수정하기
      .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
      .bodyToMono(responseClass)
      .block();
  }
}
