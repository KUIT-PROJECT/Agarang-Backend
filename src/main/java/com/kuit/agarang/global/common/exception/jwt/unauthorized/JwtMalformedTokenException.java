package com.kuit.agarang.global.common.exception.jwt.unauthorized;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtMalformedTokenException extends JwtUnauthorizedTokenException {

  private final ResponseStatus exceptionStatus;

  public JwtMalformedTokenException(ResponseStatus exceptionStatus) {
    super(exceptionStatus);
    this.exceptionStatus = exceptionStatus;
  }

}
