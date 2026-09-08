package com.babakalizada.wallet.service;

import com.babakalizada.wallet.enums.CurrencyType;

import java.math.BigDecimal;

public interface ITopUpPaymentService {
    TopUpPaymentResult createTopUpPayment(
            Long userId,
            Long walletId,
            BigDecimal amount,
            CurrencyType currency
    );
}
