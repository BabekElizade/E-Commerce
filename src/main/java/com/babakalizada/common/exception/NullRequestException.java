package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class NullRequestException extends BaseException {
    public NullRequestException(ErrorMessage message) {
        super(message, ErrorCode.NULL_REQUEST);
    }
}
