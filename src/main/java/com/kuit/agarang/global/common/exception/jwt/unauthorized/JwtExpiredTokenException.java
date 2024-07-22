package com.kuit.agarang.global.common.exception.jwt.unauthorized;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtExpiredTokenException extends JwtUnauthorizedTokenException {

  private final ResponseStatus exceptionStatus;

  public JwtExpiredTokenException(ResponseStatus exceptionStatus) {
    super(exceptionStatus);
    this.exceptionStatus = exceptionStatus;
  }

}
