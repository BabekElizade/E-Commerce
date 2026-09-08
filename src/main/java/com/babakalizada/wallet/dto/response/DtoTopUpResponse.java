package com.babakalizada.wallet.dto.response;

import com.babakalizada.wallet.enums.CurrencyType;
import com.babakalizada.wallet.enums.WalletTransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoTopUpResponse {

    private Long paymentId;

    private BigDecimal amount;

    private CurrencyType currency;

    private WalletTransactionType transactionType;
}