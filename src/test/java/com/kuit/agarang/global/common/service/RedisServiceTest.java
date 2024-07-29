package com.kuit.agarang.global.common.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class RedisServiceTest {

  @Autowired
  private RedisService redisService;
  private static final String KEY = "agarang";

  @AfterEach
  void delete() {
    redisService.delete(KEY);
  }

  @Test
  void saveStringAndGet() {
    // given
    String value = "test-value";
    // when
    redisService.save(KEY, value);
    // then
    assertEquals(value, redisService.get(KEY, String.class));
  }

  @Test
  void updateString() {
    // given
    String value = "test-value";
    redisService.save(KEY, value);
    // when
    String newValue = "test2-value";
    redisService.update(KEY, newValue);
    // then
    assertEquals(newValue, redisService.get(KEY, String.class));
  }

  @Test
  void saveStringWithExpiry() throws InterruptedException {
    // given
    String value = "test-value";
    redisService.saveWithExpiry(KEY, value, 1000L, TimeUnit.MILLISECONDS);
    // when
    Thread.sleep(1000);
    // then
    assertNull(redisService.get(KEY, String.class));
  }

  @Test
  void deleteString() {
    // given
    String value = "test-value";
    redisService.save(KEY, value);
    // when
    redisService.delete(KEY);
    // then
    assertNull(redisService.get(KEY, String.class));
  }
}