package com.kuit.agarang.global.discord.interceptor;

import com.kuit.agarang.global.discord.model.dto.HeaderInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Profile("!local")
@Component
public class ProdErrorInterceptor implements HandlerInterceptor {

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    if (ex != null) {
      MDC.clear();
      HeaderInfo headerInfo = HeaderInfo.of(request);
      MDC.put("host", headerInfo.getHost());
      MDC.put("cookie", headerInfo.getCookie());
      MDC.put("method", headerInfo.getMethod());
      MDC.put("url", headerInfo.getUrl());
      MDC.put("query", headerInfo.getQueryString());
    }
  }
}
