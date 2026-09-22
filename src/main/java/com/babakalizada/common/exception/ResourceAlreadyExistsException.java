package com.babakalizada.common.exception;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;

public class ResourceAlreadyExistsException extends BaseException {
    public ResourceAlreadyExistsException(ErrorMessage message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXISTS);
    }
}
