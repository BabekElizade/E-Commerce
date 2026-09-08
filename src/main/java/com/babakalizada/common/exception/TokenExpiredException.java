package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException(ErrorMessage message) {
        super(message, ErrorCode.TOKEN_EXPIRED);
    }
}
