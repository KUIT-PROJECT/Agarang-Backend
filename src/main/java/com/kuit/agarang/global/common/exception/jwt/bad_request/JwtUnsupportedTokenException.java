package com.kuit.agarang.global.common.exception.jwt.bad_request;

import com.kuit.agarang.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtUnsupportedTokenException extends JwtBadRequestException {

  private final ResponseStatus exceptionStatus;

  public JwtUnsupportedTokenException(ResponseStatus exceptionStatus) {
    super(exceptionStatus);
    this.exceptionStatus = exceptionStatus;
  }

}
