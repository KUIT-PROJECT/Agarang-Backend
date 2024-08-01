package com.kuit.agarang.global.common.exception.handler;

import com.kuit.agarang.global.common.exception.exception.BusinessException;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.common.model.dto.HeaderInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBusinessException(BusinessException e) {
    return ResponseEntity.status(e.getBaseResponseStatus().getHttpStatus())
      .body(new BaseResponse<>(e.getBaseResponseStatus()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleException(
    HttpServletRequest request, HttpServletResponse response, Exception e) {
    MDC.clear();
    HeaderInfo headerInfo = HeaderInfo.of(request);
    MDC.put("host", headerInfo.getHost());
    MDC.put("cookie", headerInfo.getCookie());
    MDC.put("method", headerInfo.getMethod());
    MDC.put("uri", headerInfo.getUri());
    MDC.put("query", headerInfo.getQueryString());
    MDC.put("details", Arrays.toString(e.getStackTrace()));
    LOG.error(e.getMessage());
    return ResponseEntity.status(response.getStatus())
      .body(new BaseResponse<>(BaseResponseStatus.SERVER_ERROR));
  }
}
