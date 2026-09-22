package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class InvalidOrderStatusException extends BaseException {
    public InvalidOrderStatusException(ErrorMessage message) {
        super(message, ErrorCode.INVALID_ORDER_STATUS);
    }
}
