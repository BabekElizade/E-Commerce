package com.babakalizada.common.constant;

import com.babakalizada.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private final ErrorCode errorCode;
    private final String detail;

    public String prepareMessage() {

        StringBuilder message = new StringBuilder();
        message.append(errorCode.getMessage());

        if (detail != null && !detail.isBlank()) {
            message.append(" : ").append(detail);
        }

        return message.toString();
    }
}