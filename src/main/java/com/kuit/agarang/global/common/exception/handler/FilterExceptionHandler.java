package com.kuit.agarang.global.common.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class FilterExceptionHandler extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (BusinessException e) {
      response.setStatus(e.getBaseResponseStatus().getHttpStatus().value());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter()
        .write(objectMapper.writeValueAsString(new BaseResponse<>(e.getBaseResponseStatus())));
    }
  }
}
