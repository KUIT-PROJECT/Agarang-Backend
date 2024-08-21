package com.kuit.agarang.global.common.exception.exception;

import com.kuit.agarang.global.common.model.dto.BaseResponseStatus;
import lombok.Getter;

@Getter
public class JWTException extends BusinessException {
  public JWTException(BaseResponseStatus baseResponseStatus) {
    super(baseResponseStatus);
  }
}
