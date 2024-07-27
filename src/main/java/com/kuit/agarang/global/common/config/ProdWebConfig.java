package com.kuit.agarang.global.common.config;

import com.kuit.agarang.global.discord.interceptor.ProdErrorInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("!local")
@Configuration
@RequiredArgsConstructor
public class ProdWebConfig implements WebMvcConfigurer {

  private final ProdErrorInterceptor prodErrorInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(prodErrorInterceptor);
  }
}
