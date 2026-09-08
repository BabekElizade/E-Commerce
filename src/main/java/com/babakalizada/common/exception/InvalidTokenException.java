package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(ErrorMessage message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}
