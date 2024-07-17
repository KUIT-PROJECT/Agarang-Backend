package com.kuit.agarang.global.common.exception.jwt.unauthorized;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtInvalidTokenException extends JwtUnauthorizedTokenException {

  private final ResponseStatus exceptionStatus;

  public JwtInvalidTokenException(ResponseStatus exceptionStatus) {
    super(exceptionStatus);
    this.exceptionStatus = exceptionStatus;
  }

}
