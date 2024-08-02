package com.kuit.agarang.global.common.exception.handler;

import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import com.kuit.agarang.global.common.model.dto.HeaderInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleException(MaxUploadSizeExceededException e) {
    return ResponseEntity.status(e.getStatusCode())
      .body(new BaseResponse<>(BaseResponseStatus.FILE_TOO_LARGE));
  }

  @ExceptionHandler(RedisConnectionFailureException.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleRedisConnectionFailure(RedisConnectionFailureException e) {
    LOG.info(e.getMessage());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
      .body(new BaseResponse<>(BaseResponseStatus.FAIL_REDIS_CONNECTION));
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
    e.printStackTrace();
    return ResponseEntity.status(response.getStatus())
      .body(new BaseResponse<>(BaseResponseStatus.SERVER_ERROR));
  }
}
