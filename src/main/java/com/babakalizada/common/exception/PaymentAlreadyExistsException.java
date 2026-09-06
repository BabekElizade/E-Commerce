package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class PaymentAlreadyExistsException extends BaseException {

    public PaymentAlreadyExistsException(ErrorMessage message) {
        super(message, ErrorCode.PAYMENT_ALREADY_EXISTS);
    }
}
