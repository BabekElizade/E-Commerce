package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException(ErrorMessage message) {
        super(message, ErrorCode.INSUFFICIENT_STOCK);
    }
}
