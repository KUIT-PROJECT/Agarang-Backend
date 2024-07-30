package com.kuit.agarang.global.common.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
  SUCCESS(true, HttpStatus.OK, 200, "요청에 성공하였습니다."),
  BAD_REQUEST(false, HttpStatus.BAD_REQUEST, 400, "입력값을 확인해주세요."),
  UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
  FORBIDDEN(false, HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
  NOT_FOUND(false, HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),

  INVALID_MEMORY_ID(false,HttpStatus.NOT_FOUND, 4001, "존재하지 않는 추억입니다."),

  ;


  private final boolean isSuccess;
  @JsonIgnore
  private final HttpStatus httpStatus;
  private final int code;
  private final String message;

  BaseResponseStatus(boolean isSuccess, HttpStatus httpStatus, int code, String message) {
    this.isSuccess = isSuccess;
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}
