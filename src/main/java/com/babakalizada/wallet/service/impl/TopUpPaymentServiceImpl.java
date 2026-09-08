package com.babakalizada.wallet.service.impl;

import com.babakalizada.wallet.enums.CurrencyType;
import com.babakalizada.wallet.service.ITopUpPaymentService;
import com.babakalizada.wallet.service.TopUpPaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TopUpPaymentServiceImpl
        implements ITopUpPaymentService {

    @Override
    public TopUpPaymentResult createTopUpPayment(
            Long userId,
            Long walletId,
            BigDecimal amount,
            CurrencyType currency
    ){
        return null;
    }
}
