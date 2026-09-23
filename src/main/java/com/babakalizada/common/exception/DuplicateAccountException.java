package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class DuplicateAccountException extends BaseException {
    public DuplicateAccountException(ErrorMessage message) {
        super(message, ErrorCode.DUPLICATE_ACCOUNT);
    }
}
