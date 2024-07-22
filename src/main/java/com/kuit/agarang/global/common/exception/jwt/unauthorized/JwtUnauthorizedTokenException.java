package com.kuit.agarang.global.common.exception.jwt.unauthorized;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtUnauthorizedTokenException extends RuntimeException {

  private final ResponseStatus exceptionStatus;

  public JwtUnauthorizedTokenException(ResponseStatus exceptionStatus) {
    super(exceptionStatus.getMessage());
    this.exceptionStatus = exceptionStatus;
  }

}
