package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class PasswordMatchException extends BaseException {
    public PasswordMatchException(ErrorMessage message) {
        super(message, ErrorCode.PASSWORD_MISMATCH);
    }
}
