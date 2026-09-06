package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class BusinessException extends BaseException {

    public BusinessException(ErrorMessage message) {
        super(message, ErrorCode.BUSINESS_ERROR);
    }
}
