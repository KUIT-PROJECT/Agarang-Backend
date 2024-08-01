package com.kuit.agarang.global.common.exception.handler;

import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleException(MaxUploadSizeExceededException e) {
    return ResponseEntity.status(e.getStatusCode())
      .body(new BaseResponse<>(BaseResponseStatus.FILE_TOO_LARGE));
  }
}
