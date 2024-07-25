package com.kuit.agarang.global.common.exception.jwt.bad_request;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtBadRequestException extends RuntimeException {

  private final ResponseStatus exceptionStatus;

  public JwtBadRequestException(ResponseStatus exceptionStatus) {
    super(exceptionStatus.getMessage());
    this.exceptionStatus = exceptionStatus;
  }

}
