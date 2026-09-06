package com.babakalizada.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}
