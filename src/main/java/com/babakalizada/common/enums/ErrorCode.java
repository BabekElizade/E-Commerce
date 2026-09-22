package com.babakalizada.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND(
            "RESOURCE_NOT_FOUND",
            "Resource not found"
    ),
    ACCESS_DENIED(
            "ACCESS_DENIED",
            "You cannot update another user's review!"
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
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "User not found"
    ),
    STOCK_NOT_ENOUGH(
            "STOCK_NOT_ENOUGH",
            "Stock not enough"
    ),
    REVIEW_ALREADY_EXISTS(
            "REVIEW_ALREADY_EXISTS",
            "Review Already Exist"
    ),
    INVALID_ORDER_STATUS(
            "INVALID_ORDER_STATUS",
            "Invalid Order Status"
    ),
    NULL_REQUEST(
            "NULL_REQUEST",
            "Null Request"
    ),
    INVALID_TOKEN(
            "INVALID_TOKEN",
            "Token is invalid"
    ), RESOURCE_ALREADY_EXISTS(
            "RESOURCE_ALREADY_EXISTS",
            "Resource Already Exist"
    );

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
