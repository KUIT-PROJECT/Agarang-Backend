package com.kuit.agarang.global.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class CodeUtil {

  public static String generateUniqueCode() {
    return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
  }
}
