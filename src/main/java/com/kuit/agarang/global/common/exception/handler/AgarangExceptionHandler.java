package com.kuit.agarang.global.common.exception.handler;

import com.kuit.agarang.global.common.exception.exception.AgarangException;
import com.kuit.agarang.global.common.model.dto.BaseResponse;
import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class AgarangExceptionHandler {

  @ExceptionHandler(AgarangException.class)
  public ResponseEntity<BaseResponse<BaseResponseStatus>> handleAgarangException(AgarangException e) {
    return ResponseEntity.status(e.getBaseResponseStatus().getHttpStatus())
            .body(new BaseResponse(e.getBaseResponseStatus()));
  }

}
