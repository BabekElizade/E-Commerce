package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorMessage errorMessage, ErrorCode errorCode) {
        super(errorMessage.prepareMessage());
        this.errorCode = errorCode;
    }
}
