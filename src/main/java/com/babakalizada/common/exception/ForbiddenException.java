package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class ForbiddenException extends BaseException {

    public ForbiddenException(ErrorMessage message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}
