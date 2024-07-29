package com.kuit.agarang.global.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  public void save(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public <T> T get(String key, Class<T> clazz) {
    Object value = redisTemplate.opsForValue().get(key);
    if (value == null) return null;

    try {
      return objectMapper.convertValue(value, clazz);
    } catch (Exception e) {
      // TODO : custom exception 일괄 수정
      throw new RuntimeException("Failed to convert value from Redis", e);
    }
  }

  public void update(String key, Object newValue) {
    redisTemplate.opsForValue().set(key, newValue);
  }

  public boolean existsByKey(String key) {
    return redisTemplate.hasKey(key);
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }

  public void saveWithExpiry(String key, String value, long timeout, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
  }
}
