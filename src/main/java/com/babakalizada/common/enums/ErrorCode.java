package com.babakalizada.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND(
            "RESOURCE_NOT_FOUND",
            "Resource not found"
    ),

    BUSINESS_ERROR(
            "BUSINESS_ERROR",
            "Business rule violation"
    ),

    FORBIDDEN(
            "FORBIDDEN",
            "Access denied"
    ),

    INSUFFICIENT_BALANCE(
            "INSUFFICIENT_BALANCE",
            "User balance is not enough"
    ),

    INSUFFICIENT_STOCK(
            "INSUFFICIENT_STOCK",
            "Product stock is not enough"
    ),

    TOKEN_EXPIRED(
            "TOKEN_EXPIRED",
            "Token expired"
    ),

    PAYMENT_ALREADY_EXISTS(
            "PAYMENT_ALREADY_EXISTS",
            "Payment already exists"
    ),
    INVALID_TOKEN(
            "INVALID_TOKEN",
            "Token is invalid"
    );

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
