package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class InsufficientBalanceException extends BaseException {

    public InsufficientBalanceException(ErrorMessage message) {
        super(message, ErrorCode.INSUFFICIENT_BALANCE);
    }
}
